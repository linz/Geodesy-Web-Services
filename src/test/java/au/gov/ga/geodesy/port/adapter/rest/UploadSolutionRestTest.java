package au.gov.ga.geodesy.port.adapter.rest;

import static au.gov.ga.geodesy.port.adapter.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.PositionRepository;
import au.gov.ga.geodesy.support.TestResources;

public class UploadSolutionRestTest extends RestTest {

    private static final Logger log = LoggerFactory.getLogger(UploadSolutionRestTest.class);

    @Autowired
    private PositionRepository positions;

    @Test
    @Rollback(false)
    public void uploadALIC() throws Exception {
        String content = FileUtils.readFileToString(TestResources.originalSopacSiteLog("ALIC"), Charset.defaultCharset());
        mvc.perform(post("/siteLog/sopac/upload").contentType(MediaType.APPLICATION_XML).content(content))
            .andExpect(status().isOk());
    }

    @Test(dependsOnMethods = "uploadALIC")
    @Rollback(false)
    public void uploadSolutions() throws Exception {
        for (File file : weeklyFinalSolutions()) {
            log.info("Uploading " + file);
            mvc.perform(post("/solution/weekly/upload?sinexFileName=" + file)).andDo(print);
        }
    }

    @Test(dependsOnMethods = "uploadSolutions")
    public void countPositions() throws Exception {
        assertEquals(positions.count(), 16965);
    }
}
