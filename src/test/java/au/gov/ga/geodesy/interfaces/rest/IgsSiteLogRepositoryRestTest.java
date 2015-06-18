package au.gov.ga.geodesy.interfaces.rest;

import java.io.File;
import java.io.FileReader;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.Assert;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;

@Transactional
public class IgsSiteLogRepositoryRestTest extends RestTest {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogRepositoryRestTest.class);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private IgsSiteLogXmlMarshaller moxy;

    @Test
    @Rollback(false)
    public void testSaveAlic() throws Exception {
        IgsSiteLog alic = moxy.unmarshal(new FileReader(getSiteLog("ALIC")));
        siteLogs.save(alic);
    }

    @Test(dependsOnMethods = {"testSaveAlic"})
    public void fetchAlic() throws Exception {
        Assert.assertNotNull(siteLogs.findByFourCharacterId("ALIC"));
    }

    private File getSiteLog(String fourCharacterId) {
        return new File("src/test/resources/sitelog/" + fourCharacterId.toUpperCase() + ".xml");
    }

    /* @AfterClass */
    public static void sleepUntilInterrupted() {
        log.info("Tests are done, going to sleep.");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ok) {}
    }
}
