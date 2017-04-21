package au.gov.ga.geodesy.port.adapter.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.domain.model.CorsNetwork;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class CorsNetworkEndpointITest extends IntegrationTest {

    @Autowired
    @Qualifier("_halObjectMapper")
    private ObjectMapper mapper;

    @Test
    public void createEmptyCorsNetwork() throws Exception {
        String expectedErrorMessage =
            "not-null property references a null or transient value : au.gov.ga.geodesy.domain.model.CorsNetwork.name";

        mvc.perform(post("/corsNetworks").content("{}"))
            .andDo(ResultHandlers.print)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.cause.message").value(expectedErrorMessage));
    }

    @Test
    @Rollback(false)
    public void createCorsNetwork() throws Exception {
        mvc.perform(post("/corsNetworks").content("{\"name\": \"NSW\", \"description\": \"New South Wales CORS Network\"}"))
            .andDo(document("createCorsNetwork"))
            .andExpect(status().isCreated());
    }

    @Test(dependsOnMethods = {"createCorsNetwork"})
    @Rollback(false)
    public void findCorsNetworkByName() throws Exception {
        String response = mvc.perform(get("/corsNetworks/search/findByName?name=NSW"))
            .andExpect(status().isOk())
            .andDo(document("findCorsNetworkByName"))
            .andDo(ResultHandlers.print)
            .andReturn().getResponse().getContentAsString();

        assertThat(mapper.readValue(response, CorsNetwork.class).getDescription(), is("New South Wales CORS Network"));
    }
}
