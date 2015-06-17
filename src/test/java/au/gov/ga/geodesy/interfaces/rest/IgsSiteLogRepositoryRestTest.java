package au.gov.ga.geodesy.interfaces.rest;

import static au.gov.ga.geodesy.interfaces.rest.ResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.File;
import java.io.FileReader;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {RestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
@Transactional
public class IgsSiteLogRepositoryRestTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogRepositoryRestTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static MockMvc mvc;

    @Autowired
    private IgsSiteLogRepository siteLogs;

    private IgsSiteLogXmlMarshaller moxy;

    @BeforeClass
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        moxy = new IgsSiteLogMoxyMarshaller();
    }

    @Test
    @Rollback(false)
    public void testSaveAlic() throws Exception {
        IgsSiteLog alic = moxy.unmarshal(new FileReader(getSiteLog("ALIC")));
        siteLogs.save(alic);
    }

    @Test(dependsOnMethods = {"testSaveAlic"})
    public void fetchAlic() throws Exception {
        mvc.perform(get("/igsSiteLogs")).andDo(print);
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
