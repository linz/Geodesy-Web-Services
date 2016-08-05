package au.gov.ga.geodesy.domain.service;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.spring.IntegrationTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@Transactional("geodesyTransactionManager")
public class MultipleSitesTest extends IntegrationTestConfig {

    private static final String scenarioDirName = "src/test/resources/multiple-sites/";

    @Autowired
    private CorsSiteLogService siteLogService;

    @Autowired
    private SiteLogRepository siteLogs;

    private int numberOfSites;

    /**
     * Each siteLog file must have a unique getSiteIdentification.
     * @param scenarioDirName
     * @throws InvalidSiteLogException
     * @throws IOException
     */
    private void executeSiteLogScenario(String scenarioDirName) throws IOException, InvalidSiteLogException {
        File[] siteLogFiles = new File(scenarioDirName).listFiles((File dir, String f) -> {
            return f.endsWith(".xml");
        });
        numberOfSites = siteLogFiles.length;
        for (File f : siteLogFiles) {
            SiteLogReader input = new SopacSiteLogReader(new FileReader(f));
            siteLogService.upload(input.getSiteLog());
        }
    }

    @Test
    @Rollback(false)
    public void checkSetupId() throws Exception {
        assertThat(siteLogs.count(), equalTo(0L));
        executeSiteLogScenario(scenarioDirName);
        assertThat(siteLogs.count(), equalTo(Integer.toUnsignedLong(numberOfSites)));
    }
}
