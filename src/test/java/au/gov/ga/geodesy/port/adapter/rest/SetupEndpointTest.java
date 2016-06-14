package au.gov.ga.geodesy.port.adapter.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.CorsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;

public class SetupEndpointTest extends RestTest {

    @Autowired
    private CorsSiteLogService siteLogService;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog alice = new SopacSiteLogReader(TestResources.sopacSiteLogReader("ALIC")).getSiteLog();
        SiteLog zimmerwald = new SopacSiteLogReader(TestResources.sopacSiteLogReader("ZIMM")).getSiteLog();
        siteLogService.upload(alice);
        siteLogService.upload(zimmerwald);
    }

    @Test(dependsOnMethods = {"upload"})
    @Rollback(false)
    public void testFindCurrentByFourCharacterId() throws Exception {
        mvc.perform(get("/setups/search/findCurrentByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk());
    }

    @Test(dependsOnMethods = {"upload"})
    @Rollback(false)
    public void testFindByFourCharacterIdAndDate() throws Exception {
        mvc.perform(get("/setups/search/findByFourCharacterId?id=ALIC&effectiveFrom=2011-12-12&effectiveTo=2014-12-12&timeFormat=uuuu-MM-dd"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page.totalElements").value(3));
    }

    @Test(dependsOnMethods = {"upload"})
    public void testDateFormat() throws Exception {
        mvc.perform(get("/setups/search/findByFourCharacterId?id=ALIC&effectiveFrom=2011-12-12&effectiveTo=2011-12-12&timeFormat=uuuu-MM-dd"))
            .andExpect(jsonPath("$._embedded.setups[0].effectivePeriod.from").value("2011-07-20T00:00:00Z"))
            .andExpect(jsonPath("$._embedded.setups[0].effectivePeriod.to").value("2013-03-08T00:00:00Z"));
    }
}
