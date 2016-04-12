package au.gov.ga.geodesy.port.adapter.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.Test;

public class ValidateSiteLogsRestTest extends RestTest {

    private String readFile(String resourceName) throws FileNotFoundException, IOException {
        return IOUtils.toString(new FileInputStream(ResourceUtils.getFile(resourceName)));
    }

    @Test
    @Rollback(false)
    public void validateMOBS() throws Exception {
        String siteLogText = readFile("classpath:sitelog/geodesyml/MOBS.xml");
        mvc.perform(post("/siteLog/validate").contentType(MediaType.APPLICATION_XML).content(siteLogText))
            .andExpect(status().isOk());
    }

    @Test
    @Rollback(false)
    public void invalidateMOBS() throws Exception {
        String siteLogText = readFile("classpath:sitelog/geodesyml/MOBS-invalid-schema.xml");
        mvc.perform(post("/siteLog/validate").contentType(MediaType.APPLICATION_XML).content(siteLogText))
            .andExpect(status().isBadRequest())
            .andDo(ResultHandlers.print);
    }
}
