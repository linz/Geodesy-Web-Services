package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesyServiceTestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class ChangeReceiverAtABRKTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String scenarioDirName = "src/test/resources/change-receiver-at-abrk/";

    @Autowired
    private IgsSiteLogService siteLogService;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;
    
    @Autowired
    private IgsSiteLogRepository siteLogs;

    private void executeSiteLogScenario(String scenarioDirName) throws FileNotFoundException, MarshallingException {
        File[] siteLogFiles = new File(scenarioDirName).listFiles((File dir, String f) -> {
            return f.endsWith(".xml");
        });
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
        // TODO: check
        siteLogMembers = siteLogs.findAll();
        Assert.assertEquals(1, siteLogMembers.size());
    }
}
