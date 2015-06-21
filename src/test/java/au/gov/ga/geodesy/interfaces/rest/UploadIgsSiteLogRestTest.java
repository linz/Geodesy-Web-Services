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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@Transactional("geodesyTransactionManager")
public class UploadIgsSiteLogRestTest extends RestTest {

    private static final Logger log = LoggerFactory.getLogger(UploadIgsSiteLogRestTest.class);

    @Test
    @Rollback(false)
    public void uploadAlic() throws Exception {
        String content = FileUtils.readFileToString(getSiteLog("ALIC"), Charset.defaultCharset());
        mvc.perform(post("/siteLog/upload").contentType(MediaType.APPLICATION_XML).content(content))
            .andExpect(status().isOk());
    }

    @Test(dependsOnMethods = {"uploadAlic"})
    public void fetchAlic() throws Exception {
        mvc.perform(get("/gnssCorsSites")).andDo(print);
    }

    private File getSiteLog(String fourCharacterId) {
        return new File("src/test/resources/sitelog/" + fourCharacterId.toUpperCase() + ".xml");
    }

    @AfterClass
    public static void sleepUntilInterrupted() {
        log.info("Tests are done, going to sleep.");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ok) {}
    }
}
