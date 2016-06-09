package au.gov.ga.geodesy.port.adapter.rest;

import static au.gov.ga.geodesy.port.adapter.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;

public class UploadAliceGeodesyMLSiteLogRestTest extends RestTest {

    @Test
    @Rollback(false)
    public void uploadALIC() throws Exception {
        String content = IOUtils.toString(TestResources.geodesyMLSiteLogReader("ALIC"));
        mvc.perform(post("/siteLog/upload").contentType(MediaType.APPLICATION_XML).content(content))
            .andExpect(status().isCreated());
    }

    @Test(dependsOnMethods = {"uploadALIC"})
    public void checkALIC() throws Exception {
        mvc.perform(get("/corsSites/search/findByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk())
            .andDo(print);
    }

    // TODO: checkout uploaded site log, site, setups, equipment, and nodes
}
