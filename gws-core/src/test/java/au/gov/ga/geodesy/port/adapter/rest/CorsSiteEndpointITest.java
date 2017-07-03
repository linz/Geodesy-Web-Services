package au.gov.ga.geodesy.port.adapter.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.domain.model.CorsSite;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.CorsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class CorsSiteEndpointITest extends IntegrationTest {

    @Autowired
    private CorsSiteLogService siteLogService;

    @Autowired
    @Qualifier("_halObjectMapper")
    private ObjectMapper mapper;

    private CorsSite alice;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog alice = new SopacSiteLogReader(TestResources.originalSopacSiteLogReader("ALIC")).getSiteLog();
        siteLogService.upload(alice);
    }

    @Test(dependsOnMethods = {"upload"})
    @Rollback(false)
    public void testFindByFourCharacterId() throws Exception {
        String response = mvc.perform(get("/corsSites/search/findByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links.self").isNotEmpty())
            .andDo(document("findByFourCharacterId"))
            .andReturn().getResponse().getContentAsString();

        alice = mapper.readValue(response, CorsSite.class);
        assertThat(alice.getSiteStatus(), is("PUBLIC"));
    }

    @Test(dependsOnMethods = {"testFindByFourCharacterId"})
    @Rollback(false)
    public void testPatchSiteStatus() throws Exception {
        mvc.perform(patch("/corsSites/" + alice.getId())
            .with(super.superuserToken())
            .content("{\"siteStatus\": \"PRIVATE\"}"))
            .andDo(document("patchCorsSite"))
            .andExpect(status().isNoContent());
    }

    @Test(dependsOnMethods = {"testPatchSiteStatus"})
    @Rollback(false)
    public void checkSiteStatus() throws Exception {
        String response = mvc.perform(get("/corsSites/search/findByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        assertThat(mapper.readValue(response, CorsSite.class).getSiteStatus(), is("PRIVATE"));
    }
}
