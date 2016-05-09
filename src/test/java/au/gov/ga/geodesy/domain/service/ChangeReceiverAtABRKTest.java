package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogSource;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, GeodesyServiceTestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class ChangeReceiverAtABRKTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String scenarioDirName = "src/test/resources/change-receiver-at-abrk/";

    @Autowired
    private IgsSiteLogService siteLogService;

    @Autowired
    private SiteLogRepository siteLogs;

    private void executeSiteLogScenario(String scenarioDirName) throws FileNotFoundException, InvalidSiteLogException {
        File[] siteLogFiles = new File(scenarioDirName).listFiles((File dir, String f) -> {
            return f.endsWith(".xml");
        });
        for (File siteLogFile : siteLogFiles) {
            SiteLogSource input = new SopacSiteLogReader(new FileReader(siteLogFile));
            siteLogService.upload(input.getSiteLog());
        }
    }

    @Test
    @Rollback(false)
    public void checkSetupId() throws Exception {
        Assert.assertEquals(0, siteLogs.count());
        executeSiteLogScenario(scenarioDirName);
        Assert.assertEquals(1, siteLogs.count());
    }
}
