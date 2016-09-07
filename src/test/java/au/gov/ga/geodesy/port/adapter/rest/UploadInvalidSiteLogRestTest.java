package au.gov.ga.geodesy.port.adapter.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;

public class UploadInvalidSiteLogRestTest extends RestTest {

    @Test
    public void uploadInvalidSopacSiteLog() throws Exception {
        String response = mvc.perform(post("/siteLogs/sopac/upload").contentType(MediaType.APPLICATION_XML).content("Invalid site log content"))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        MatcherAssert.assertThat(response, Matchers.not(Matchers.isEmptyOrNullString()));
    }
}
