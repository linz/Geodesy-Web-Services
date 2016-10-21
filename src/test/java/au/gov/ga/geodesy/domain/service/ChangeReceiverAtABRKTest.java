package au.gov.ga.geodesy.domain.service;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.spring.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


@Transactional("geodesyTransactionManager")
public class ChangeReceiverAtABRKTest extends IntegrationTest {

    private static final String scenarioDirName = "src/test/resources/change-receiver-at-abrk/";

    @Autowired
    private CorsSiteLogService siteLogService;

    @Autowired
    private SiteLogRepository siteLogs;

    private void executeSiteLogScenario(String scenarioDirName) throws FileNotFoundException, InvalidSiteLogException {
        File[] siteLogFiles = new File(scenarioDirName).listFiles((File dir, String f) -> {
            return f.endsWith(".xml");
        });
        for (File siteLogFile : siteLogFiles) {
            SiteLogReader input = new SopacSiteLogReader(new FileReader(siteLogFile));
            siteLogService.upload(input.getSiteLog());
        }
    }

    @Test
    @Rollback(false)
    public void checkSetupId() throws Exception {
        assertThat(siteLogs.count(), equalTo(0L));
        executeSiteLogScenario(scenarioDirName);
        assertThat(siteLogs.count(), equalTo(1L));
    }
}
