package au.gov.ga.geodesy.port.adapter.rest;

import static au.gov.ga.geodesy.port.adapter.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;

public class UploadAliceIgsSiteLogRestTest extends RestTest {

    @Test
    @Rollback(false)
    public void uploadALIC() throws Exception {
        String content = FileUtils.readFileToString(TestResources.sopacSiteLog("ALIC"), Charset.defaultCharset());
        mvc.perform(post("/siteLog/sopac/upload").contentType(MediaType.APPLICATION_XML).content(content))
            .andExpect(status().isOk());
    }

    @Test(dependsOnMethods = {"uploadALIC"})
    public void printSiteLog() throws Exception {
        mvc.perform(get("/siteLogs/search/findByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk())
            .andDo(print);
    }

    @Test(dependsOnMethods = {"uploadALIC"})
    public void printSite() throws Exception {
        mvc.perform(get("/corsSites/search/findByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk())
            .andDo(print);
    }
}
