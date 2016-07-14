package au.gov.ga.geodesy.port.adapter.rest;

import static au.gov.ga.geodesy.port.adapter.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import java.lang.reflect.Method;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.CorsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;

/*
 * Tests for GnssReceiver HTTP endpoint.
 */
public class GnssReceiverEndpointTest extends RestTest {

    private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation("target/generated-snippets");

    @Autowired
    private CorsSiteLogService siteLogService;

    @BeforeMethod
    public void setUp(Method method) {
        mvc = MockMvcBuilders.webAppContextSetup(super.webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();

        restDocumentation.beforeTest(getClass(), method.getName());
    }

    @AfterMethod
    public void tearDown() {
        restDocumentation.afterTest();
    }

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog zimmerwald = new SopacSiteLogReader(TestResources.originalSopacSiteLogReader("ZIMM")).getSiteLog();
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
            .andExpect(jsonPath("$._embedded.gnssReceivers[0]._links.self").isNotEmpty())
            .andDo(document("findReceiversByType",
                links(halLinks(),
                    linkWithRel("self").description("Link to ..."),
                    linkWithRel("profile").description("Link to ...")
                ),
                relaxedResponseFields(
                    fieldWithPath("_embedded.gnssReceivers[0].type").description("IGS GNSS receiver type")
                )));
    }
}
