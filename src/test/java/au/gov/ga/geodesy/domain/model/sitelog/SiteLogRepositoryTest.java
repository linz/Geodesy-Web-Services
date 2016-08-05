package au.gov.ga.geodesy.domain.model.sitelog;

import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.RepositoryTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class SiteLogRepositoryTest extends RepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(SiteLogRepositoryTest.class);

    @Autowired
    private SiteLogRepository igsSiteLogs;

    @Test(groups = "first")
    @Rollback(false)
    public void saveALIC() throws Exception {
        SiteLogReader input = new SopacSiteLogReader(TestResources.originalSopacSiteLogReader("ALIC"));
        igsSiteLogs.saveAndFlush(input.getSiteLog());
    }

    /**
     * BZGN is a special case, because it does not have contact telephone numbers.
     */
    @Test(groups = "first")
    @Rollback(false)
    public void saveBZGN() throws Exception {
        SiteLogReader input = new SopacSiteLogReader(TestResources.originalSopacSiteLogReader("BZGN"));
        igsSiteLogs.saveAndFlush(input.getSiteLog());
    }

    @Test(dependsOnGroups = "first")
    @Rollback(false)
    public void saveAllSiteLogs() throws Exception {
        igsSiteLogs.deleteAll();
        for (File f : TestResources.originalSopacSiteLogs()) {
            log.info("Saving " + f.getName());
            SiteLogReader input = new SopacSiteLogReader(new InputStreamReader(new FileInputStream(f)));
            SiteLog siteLog = input.getSiteLog();
            igsSiteLogs.saveAndFlush(siteLog);
        }
    }

    @Test(dependsOnMethods = {"saveAllSiteLogs"})
    @Rollback(false)
    public void checkNumberOfSavedSiteLogs() throws Exception {
        assertThat(igsSiteLogs.count(), equalTo(681L));
    }

    @Test(dependsOnMethods = {"saveAllSiteLogs"})
    @Rollback(false)
    public void deleteSavedLogs() {
        igsSiteLogs.deleteAll();
    }

    @Test(dependsOnMethods = {"deleteSavedLogs"})
    @Rollback(false)
    public void checkNumberOfDeleteSiteLogs() throws Exception {
        assertThat(igsSiteLogs.count(), equalTo(0L));
    }
}
