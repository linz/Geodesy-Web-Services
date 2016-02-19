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
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.support.spring.GeodesyServiceUnitTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(classes = {GeodesyServiceUnitTestConfig.class,
        PersistenceJpaConfig.class}, loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class UploadAllSiteLogsTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String siteLogsDir = "src/test/resources/sitelog/";
    private File[] siteLogFiles = null;

    @Autowired
    private IgsSiteLogService service;

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    public MockEventPublisher eventPublisher;

    @BeforeClass
    private void setup() throws Exception {
        siteLogFiles = new File(siteLogsDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                /* return f.getName().startsWith("A") && f.getName().endsWith(".xml"); */
                return f.getName().endsWith(".xml");
            }
        });
    }

    private void upload() throws Exception {
        for (File f : siteLogFiles) {
            IgsSiteLog siteLog = marshaller.unmarshal(new FileReader(f));
            service.upload(siteLog);
        }
    }

    @Test
    @Rollback(true)
    public void check() throws Exception {
        upload();
        List<Event> events = eventPublisher.getPublishedEvents();
        for (Event e : events) {
            Assert.assertTrue(e instanceof SiteLogReceived);
        }
        long n = siteLogs.count();
        Assert.assertEquals(n, siteLogFiles.length);
        Assert.assertEquals(n, eventPublisher.getPublishedEvents().size());
    }
}
