package au.gov.ga.geodesy.interfaces.rest;

import static au.gov.ga.geodesy.interfaces.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.test.annotation.Rollback;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class UploadSolutionRestTest extends RestTest {

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        mvc.perform(post("/solution/weekly/upload?sinexFileName=/nas/gemd/geodesy_data/gnss/solutions/final/weekly/AUS10977.SNX")).andDo(print);
    }

    @AfterClass
    public static void sleepUntilInterrupted() {
        /* log.info("Tests are done, going to sleep."); */
        /* try { */
        /*     Thread.sleep(Long.MAX_VALUE); */
        /* } catch (InterruptedException ok) {} */
    }
}
