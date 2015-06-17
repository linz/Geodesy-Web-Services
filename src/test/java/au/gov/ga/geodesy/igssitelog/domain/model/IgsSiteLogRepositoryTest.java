package au.gov.ga.geodesy.igssitelog.domain.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class IgsSiteLogRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogRepositoryTest.class);

    @Autowired
    private IgsSiteLogRepository igsSiteLogs;

    private static final String sampleSiteLogsDir = "src/test/resources/sitelog";
    private IgsSiteLogMoxyMarshaller marshaller;

    public IgsSiteLogRepositoryTest() throws Exception {
        marshaller = new IgsSiteLogMoxyMarshaller();
    }

    @Test
    @Rollback(false)
    public void testSaveAllSiteLogs() throws Exception {
        for (File f : getSiteLogFiles()) {
            IgsSiteLog siteLog = marshaller.unmarshal(new InputStreamReader(new FileInputStream(f)));
            igsSiteLogs.saveAndFlush(siteLog);
        }
    }

    private File[] getSiteLogFiles() throws Exception {
        return new File(sampleSiteLogsDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".xml");
            }
        });
    }

    @AfterClass
    public static void sleepUntilInterrupted() {
        log.info("Tests are done, going to sleep.");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ok) {}
    }
}
