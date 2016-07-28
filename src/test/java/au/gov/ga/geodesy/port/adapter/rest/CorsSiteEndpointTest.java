package au.gov.ga.geodesy.port.adapter.rest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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

public class CorsSiteEndpointTest extends RestDocTest {

    @Autowired
    private CorsSiteLogService siteLogService;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog alice = new SopacSiteLogReader(TestResources.originalSopacSiteLogReader("ALIC")).getSiteLog();
        siteLogService.upload(alice);
    }

    @Test(dependsOnMethods = {"upload"})
    @Rollback(false)
    public void testFindByFourCharacterId() throws Exception {
        mvc.perform(get("/corsSites/search/findByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links.self").isNotEmpty())
            .andDo(document("findByFourCharacterId"));
    }
}
