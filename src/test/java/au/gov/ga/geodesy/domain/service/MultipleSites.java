package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(classes = { GeodesyServiceTestConfig.class,
		PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class MultipleSites extends AbstractTransactionalTestNGSpringContextTests {

	private static final String scenarioDirName = "src/test/resources/multiple-sites/";
																	  
	@Autowired
	private IgsSiteLogService siteLogService;

	@Autowired
	private IgsSiteLogXmlMarshaller marshaller;

	@Autowired
	private IgsSiteLogRepository siteLogs;
	
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
		for (File siteLogFile : siteLogFiles) {
			IgsSiteLog siteLog = marshaller.unmarshal(new FileReader(siteLogFile));
			siteLogService.upload(siteLog);
		}
	}

	@Test
	@Rollback(false)
	public void checkSetupId() throws Exception {
		List<IgsSiteLog> siteLogMembers = siteLogs.findAll();
		Assert.assertEquals(0, siteLogMembers.size());
		executeSiteLogScenario(scenarioDirName);
		Assert.assertEquals(numberOfSites, siteLogs.count());
	}
}
