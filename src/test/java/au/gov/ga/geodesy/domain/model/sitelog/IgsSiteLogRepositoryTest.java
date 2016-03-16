package au.gov.ga.geodesy.domain.model.sitelog;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.interfaces.SiteLogSource;
import au.gov.ga.geodesy.interfaces.sopac.SiteLogSopacSource;
import au.gov.ga.geodesy.support.spring.GeodesyServiceConfig;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class IgsSiteLogRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogRepositoryTest.class);

    @Autowired
    private IgsSiteLogRepository igsSiteLogs;

    private static final String sampleSiteLogsDir = "src/test/resources/sitelog";

    @Test
    @Rollback(false)
    public void saveAllSiteLogs() throws Exception {
        for (File f : getSiteLogFiles()) {
            SiteLogSource input = new SiteLogSopacSource(new InputStreamReader(new FileInputStream(f)));
            IgsSiteLog siteLog = input.getSiteLog();
            log.info("Saving " + siteLog.getSiteIdentification().getFourCharacterId());
            igsSiteLogs.saveAndFlush(siteLog);
        }
    }

    @Test(dependsOnMethods = {"saveAllSiteLogs"})
    @Rollback(false)
    public void checkNumberOfSavedSiteLogs() throws Exception {
        Assert.assertEquals(igsSiteLogs.count(), 681);
    }

    @Test(dependsOnMethods = {"checkNumberOfSavedSiteLogs"})
    @Rollback(false)
    public void deleteSavedLogs() {
        igsSiteLogs.deleteAll();
    }

    @Test(dependsOnMethods = {"deleteSavedLogs"})
    @Rollback(false)
    public void checkNumberOfDeleteSiteLogs() throws Exception {
        Assert.assertEquals(igsSiteLogs.count(), 0);
    }

    private File[] getSiteLogFiles() throws Exception {
        return new File(sampleSiteLogsDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".xml");
            }
        });
    }
}
