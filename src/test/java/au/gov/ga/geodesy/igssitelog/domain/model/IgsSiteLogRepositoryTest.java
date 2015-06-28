package au.gov.ga.geodesy.igssitelog.domain.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional("geodesyTransactionManager")
public class IgsSiteLogRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogRepositoryTest.class);

    @Autowired
    private IgsSiteLogRepository igsSiteLogs;

    private static final String sampleSiteLogsDir = "src/test/resources/sitelog";
    private IgsSiteLogMoxyMarshaller marshaller;

    public IgsSiteLogRepositoryTest() throws Exception {
        marshaller = new IgsSiteLogMoxyMarshaller();
    }

    /* @Test */
    /* @Rollback(false) */
    public void saveAllSiteLogs() throws Exception {
        for (File f : getSiteLogFiles()) {
            IgsSiteLog siteLog = marshaller.unmarshal(new InputStreamReader(new FileInputStream(f)));
            log.info("Saving " + siteLog.getSiteIdentification().getFourCharacterId());
            igsSiteLogs.saveAndFlush(siteLog);
        }
    }

    /* @Test(dependsOnMethods = {"saveAllSiteLogs"}) */
    public void checkNumberOfSavedSiteLogs() throws Exception {
        Assert.assertEquals(igsSiteLogs.count(), 683);
    }

    /* @Test(dependsOnMethods = {"checkNumberOfSavedSiteLogs"}) */
    /* @Rollback(false) */
    /* public void deleteSavedLogs() { */
    /*     igsSiteLogs.deleteAll(); */
    /* } */

    private File[] getSiteLogFiles() throws Exception {
        return new File(sampleSiteLogsDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".xml");
            }
        });
    }

    @AfterClass
    public static void sleepUntilInterrupted() {
        /* log.info("Tests are done, going to sleep."); */
        /* try { */
        /*     Thread.sleep(Long.MAX_VALUE); */
        /* } catch (InterruptedException ok) {} */
    }
}
