package au.gov.ga.geodesy.port.adapter.rest;

import static au.gov.ga.geodesy.port.adapter.rest.ResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class UploadAliceIgsSiteLogRestTest extends RestTest {

    @Test
    @Rollback(false)
    public void uploadALIC() throws Exception {
        String content = FileUtils.readFileToString(getSiteLog("ALIC"), Charset.defaultCharset());
        mvc.perform(post("/siteLog/upload").contentType(MediaType.APPLICATION_XML).content(content))
            .andExpect(status().isOk());
    }

    @Test(dependsOnMethods = {"uploadALIC"})
    public void checkALIC() throws Exception {
        mvc.perform(get("/corsSites/search/findByFourCharacterId?id=ALIC"))
            .andExpect(status().isOk())
            .andDo(print);
    }

    private File getSiteLog(String file) {
        return new File("src/test/resources/sitelog/" + file + ".xml");
    }
}
