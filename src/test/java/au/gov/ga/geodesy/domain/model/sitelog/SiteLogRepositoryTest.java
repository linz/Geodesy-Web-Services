package au.gov.ga.geodesy.domain.model.sitelog;

import java.io.File;
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

import au.gov.ga.geodesy.port.SiteLogSource;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class SiteLogRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(SiteLogRepositoryTest.class);

    @Autowired
    private SiteLogRepository igsSiteLogs;

    @Test(groups = "first")
    @Rollback(false)
    public void saveALIC() throws Exception {
        SiteLogSource input = new SopacSiteLogReader(TestResources.sopacSiteLogReader("ALIC"));
        igsSiteLogs.saveAndFlush(input.getSiteLog());
    }

    /**
     * BZGN is a special case, because it does not have contact telephone numbers.
     */
    @Test(groups = "first")
    @Rollback(false)
    public void saveBZGN() throws Exception {
        SiteLogSource input = new SopacSiteLogReader(TestResources.sopacSiteLogReader("BZGN"));
        igsSiteLogs.saveAndFlush(input.getSiteLog());
    }

    @Test(dependsOnGroups = "first")
    @Rollback(false)
    public void saveAllSiteLogs() throws Exception {
        igsSiteLogs.deleteAll();
        for (File f : TestResources.sopacSiteLogs()) {
            log.info("Saving " + f.getName());
            SiteLogSource input = new SopacSiteLogReader(new InputStreamReader(new FileInputStream(f)));
            SiteLog siteLog = input.getSiteLog();
            igsSiteLogs.saveAndFlush(siteLog);
        }
    }

    @Test(dependsOnMethods = {"saveAllSiteLogs"})
    @Rollback(false)
    public void checkNumberOfSavedSiteLogs() throws Exception {
        Assert.assertEquals(igsSiteLogs.count(), 681);
    }

    @Test(dependsOnMethods = {"saveAllSiteLogs"})
    @Rollback(false)
    public void deleteSavedLogs() {
        igsSiteLogs.deleteAll();
    }

    @Test(dependsOnMethods = {"deleteSavedLogs"})
    @Rollback(false)
    public void checkNumberOfDeleteSiteLogs() throws Exception {
        Assert.assertEquals(igsSiteLogs.count(), 0);
    }
}
