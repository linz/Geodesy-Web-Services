package au.gov.ga.geodesy.domain.model.sitelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class SiteLogRepositoryTest extends IntegrationTest {

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
}
