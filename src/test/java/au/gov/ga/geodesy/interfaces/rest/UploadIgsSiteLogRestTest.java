package au.gov.ga.geodesy.interfaces.rest;

import static au.gov.ga.geodesy.interfaces.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;

public class UploadIgsSiteLogRestTest extends RestTest {

    private static final Logger log = LoggerFactory.getLogger(UploadIgsSiteLogRestTest.class);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Test
    @Rollback(false)
    public void uploadABRK_0() throws Exception {
        String content = FileUtils.readFileToString(getSiteLog("ABRK_0"), Charset.defaultCharset());
        mvc.perform(post("/siteLog/upload").contentType(MediaType.APPLICATION_XML).content(content))
            .andExpect(status().isOk());
    }

    @Rollback(false)
    @Test(dependsOnMethods = {"uploadABRK_0"})
    public void uploadABRK_1() throws Exception {
        String content = FileUtils.readFileToString(getSiteLog("ABRK_1"), Charset.defaultCharset());
        mvc.perform(post("/siteLog/upload").contentType(MediaType.APPLICATION_XML).content(content))
            .andExpect(status().isOk());
    }

    @Test(dependsOnMethods = {"uploadABRK_1"})
    public void fetchAll() throws Exception {
        mvc.perform(get("/gnssCorsSites")).andDo(print);
    }

    private File getSiteLog(String file) {
        return new File("src/test/resources/sitelog/" + file + ".xml");
    }

    @AfterClass
    public static void sleepUntilInterrupted() {
        log.info("Tests are done, going to sleep.");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ok) {}
    }
}
