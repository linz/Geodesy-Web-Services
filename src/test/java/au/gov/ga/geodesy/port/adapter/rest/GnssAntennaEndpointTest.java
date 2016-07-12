package au.gov.ga.geodesy.port.adapter.rest;

import static au.gov.ga.geodesy.port.adapter.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.CorsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;

/*
 * Tests for GnssAntenna HTTP endpoint.
 */
public class GnssAntennaEndpointTest extends RestTest {

    @Autowired
    private CorsSiteLogService siteLogService;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog zimmerwald = new SopacSiteLogReader(TestResources.originalSopacSiteLogReader("ZIMM")).getSiteLog();
        siteLogService.upload(zimmerwald);
    }

    @Test(dependsOnMethods = {"upload"})
    @Rollback(false)
    public void findAntennasByType() throws Exception {
        mvc.perform(get("/gnssAntennas?serialNumber=99390"))
            .andExpect(status().isOk())
            .andDo(print)
            .andExpect(jsonPath("$.page.totalElements").value(1))
            .andExpect(jsonPath("$._embedded.gnssAntennas[0].serialNumber").value("99390"))
            .andExpect(jsonPath("$._embedded.gnssAntennas[0]._links.self").isNotEmpty());
    }
}
