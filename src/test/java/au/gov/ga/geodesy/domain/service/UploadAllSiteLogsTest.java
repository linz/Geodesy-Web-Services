package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.MockEventPublisher;
import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.support.spring.GeodesyServiceUnitTestConfig;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, GeodesyServiceUnitTestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class UploadAllSiteLogsTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String siteLogsDir = "src/test/resources/sitelog/";
    private File[] siteLogFiles = null;

    @Autowired
    private IgsSiteLogService service;

    @Autowired
    private SiteLogRepository siteLogs;

    @Autowired
    public MockEventPublisher eventPublisher;

    @BeforeClass
    private void setup() throws Exception {
        siteLogFiles = new File(siteLogsDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().startsWith("A") && f.getName().endsWith(".xml");
                /* return f.getName().endsWith(".xml"); */
            }
        });
    }

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        for (File f : siteLogFiles) {
            service.upload(new FileReader(f));
        }
    }

    @Test(dependsOnMethods = {"upload"})
    public void check() throws Exception {
        List<Event> events = eventPublisher.getPublishedEvents();
        Assert.assertEquals(events.size(), 34);
        for (Event e : events) {
            Assert.assertTrue(e instanceof SiteLogReceived);
        }
        Assert.assertEquals(0, siteLogs.count());
    }
}
