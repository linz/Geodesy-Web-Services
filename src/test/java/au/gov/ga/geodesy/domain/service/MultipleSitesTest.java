package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.ga.geodesy.port.SiteLogSource;
import au.gov.ga.geodesy.port.adapter.sopac.SiteLogSopacReader;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(classes = { GeodesySupportConfig.class, GeodesyServiceTestConfig.class,
		PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class MultipleSitesTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final String scenarioDirName = "src/test/resources/multiple-sites/";

	@Autowired
	private IgsSiteLogService siteLogService;

	@Autowired
	private SiteLogRepository siteLogs;

	private int numberOfSites;

	/**
	 * Each siteLog file must have a unique getSiteIdentification.
	 * @param scenarioDirName
	 * @throws MarshallingException
	 * @throws IOException
	 */
	private void executeSiteLogScenario(String scenarioDirName) throws MarshallingException, IOException {
		File[] siteLogFiles = new File(scenarioDirName).listFiles((File dir, String f) -> {
			return f.endsWith(".xml");
		});
		numberOfSites = siteLogFiles.length;
		for (File f : siteLogFiles) {
            SiteLogSource input = new SiteLogSopacReader(new FileReader(f));
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
