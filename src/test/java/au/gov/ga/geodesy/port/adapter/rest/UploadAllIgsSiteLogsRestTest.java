package au.gov.ga.geodesy.port.adapter.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.CorsSiteRepository;

public class UploadAllIgsSiteLogsRestTest extends RestTest {

    private static final Logger log = LoggerFactory.getLogger(UploadAllIgsSiteLogsRestTest.class);

    @Autowired
    private CorsSiteRepository sites;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        siteLogs().forEach(file -> {
            try {
                // TODO: this test takes too long, so for now we just upload some site logs
                if (file.getName().startsWith("A")) {
                    log.info("Uploading " + file.getPath());
                    String content = FileUtils.readFileToString(file, Charset.defaultCharset());
                    mvc.perform(post("/siteLog/sopac/upload").contentType(MediaType.APPLICATION_XML).content(content))
                        .andExpect(status().isOk());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test(dependsOnMethods = {"upload"})
    public void checkCount() throws Exception {
        assertEquals(sites.count(), 34);
    }
}
