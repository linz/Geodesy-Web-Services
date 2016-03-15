package au.gov.ga.geodesy.domain.model;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.interfaces.geodesyml.CorsSiteLogFactory;
import au.gov.ga.geodesy.interfaces.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GeodesyMLType;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class CorsSiteLogRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(CorsSiteLogRepositoryTest.class);

    @Autowired
    private CorsSiteLogRepository siteLogs;

    @Autowired
    private GeodesyMLMarshaller marshaller;

    private static final String sampleSiteLogsDir = "src/test/resources/sitelog/geodesyml";

    @Test
    @Rollback(false)
    public void saveAllSiteLogs() throws Exception {
        for (File f : getSiteLogFiles()) {
            GeodesyMLType geodesyML = marshaller.unmarshal(new FileReader(f)).getValue();
            new CorsSiteLogFactory(geodesyML).create()
                .forEach(siteLog -> {
                    log.info("Saving " + siteLog.getFourCharacterId());
                    siteLogs.saveAndFlush(siteLog);
                });
        }
    }

    @Test(dependsOnMethods = "saveAllSiteLogs")
    public void checkNumberOfSavedLogs() throws Exception {
        assertEquals(siteLogs.count(), 1);
    }

    private File[] getSiteLogFiles() throws Exception {
        return new File(sampleSiteLogsDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".xml");
            }
        });
    }
}
