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
 * Tests for GnssReceiver HTTP endpoint.
 */
public class GnssReceiverEndpointTest extends RestTest {

    @Autowired
    private CorsSiteLogService siteLogService;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog zimmerwald = new SopacSiteLogReader(TestResources.sopacSiteLogReader("ZIMM")).getSiteLog();
        siteLogService.upload(zimmerwald);
    }

    @Test(dependsOnMethods = {"upload"})
    @Rollback(false)
    public void findReceiversByType() throws Exception {
        mvc.perform(get("/gnssReceivers?type=TRIMBLE NETRS"))
            .andExpect(status().isOk())
            .andDo(print)
            .andExpect(jsonPath("$.page.totalElements").value(1))
            .andExpect(jsonPath("$._embedded.gnssReceivers[0].type").value("TRIMBLE NETRS"))
            .andExpect(jsonPath("$._embedded.gnssReceivers[0]._links.self").isNotEmpty());
    }
}
