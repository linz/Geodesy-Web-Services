package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import au.gov.ga.geodesy.support.spring.IntegrationTestConfig;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@Transactional("geodesyTransactionManager")
public class MultipleSitesTest extends IntegrationTestConfig {

	private static final String scenarioDirName = "src/test/resources/multiple-sites/";

	@Autowired
	private IgsSiteLogService siteLogService;

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
		Assert.assertEquals(0, siteLogs.count());
		executeSiteLogScenario(scenarioDirName);
		Assert.assertEquals(numberOfSites, siteLogs.count());
	}
}
