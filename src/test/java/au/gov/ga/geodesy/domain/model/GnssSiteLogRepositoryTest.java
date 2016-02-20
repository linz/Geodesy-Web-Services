package au.gov.ga.geodesy.domain.model;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class GnssSiteLogRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(GnssSiteLogRepositoryTest.class);

    @Autowired
    private GnssSiteLogRepository siteLogs;

    private static final String sampleSiteLogsDir = "src/test/resources/sitelog/geodesyml";

    @Test
    @Rollback(false)
    public void saveAllSiteLogs() throws Exception {
        for (File f : getSiteLogFiles()) {
            GnssSiteLog siteLog = new GnssSiteLog(Files.toString(f, Charset.defaultCharset()));
            log.info("Saving " + siteLog.getFourCharacterId());
            siteLogs.saveAndFlush(siteLog);
        }
    }

    private File[] getSiteLogFiles() throws Exception {
        return new File(sampleSiteLogsDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".xml");
            }
        });
    }
}
