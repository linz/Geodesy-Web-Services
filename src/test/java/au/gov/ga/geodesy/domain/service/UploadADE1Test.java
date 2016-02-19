package au.gov.ga.geodesy.domain.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.Node;
import au.gov.ga.geodesy.domain.model.NodeRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.domain.model.SiteIdentification;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesyServiceTestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class UploadADE1Test extends AbstractTransactionalTestNGSpringContextTests {

    private static final String siteLogsDir = "src/test/resources/sitelog/";

    private static final String fourCharId = "ADE1";

    @Autowired
    private IgsSiteLogService siteLogService;

    @Autowired
    private GnssCorsSiteService siteService;

    @Autowired
    private GnssCorsSiteRepository sites;

    @Autowired
    private SetupRepository setupRepo;

    @Autowired
    private NodeRepository nodeRepo;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    private void saveSiteLog() throws Exception {
        File siteLog = new File(siteLogsDir + fourCharId + ".xml");
        siteLogService.upload(marshaller.unmarshal(new FileReader(siteLog)));
    }

    @Test
    @Rollback(true)
    public void checkSite() throws Exception {
        saveSiteLog();

        IgsSiteLog siteLog = siteLogs.findByFourCharacterId(fourCharId);
        GnssCorsSite site = sites.findByFourCharacterId(fourCharId);
        assertNotNull(site);

        SiteIdentification identification = siteLog.getSiteIdentification();
        assertEquals(site.getName(), identification.getSiteName());
        assertEquals(site.getDateInstalled(), identification.getDateInstalled());

        List<Setup> setups = setupRepo.findBySiteId(site.getId());
        assertEquals(setups.size(), 7);

        List<Node> nodes = nodeRepo.findBySiteId(site.getId());
        assertEquals(nodes.size(), 4);
    }
}
