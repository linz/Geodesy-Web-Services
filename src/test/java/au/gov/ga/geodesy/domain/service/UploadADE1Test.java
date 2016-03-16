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

import au.gov.ga.geodesy.domain.model.CorsSite;
import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.Node;
import au.gov.ga.geodesy.domain.model.NodeRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;
import au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog;
import au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLogRepository;
import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.interfaces.SiteLogSource;
import au.gov.ga.geodesy.interfaces.sopac.SiteLogSopacSource;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, GeodesyServiceTestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class UploadADE1Test extends AbstractTransactionalTestNGSpringContextTests {

    private static final String siteLogsDir = "src/test/resources/sitelog/";

    private static final String fourCharId = "ADE1";

    @Autowired
    private IgsSiteLogService siteLogService;

    @Autowired
    private CorsSiteService siteService;

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private SetupRepository setupRepo;

    @Autowired
    private NodeRepository nodeRepo;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Test
    @Rollback(false)
    public void saveSiteLog() throws Exception {
        File f = new File(siteLogsDir + fourCharId + ".xml");
        SiteLogSource input = new SiteLogSopacSource(new FileReader(f));
        siteLogService.upload(input.getSiteLog());
    }

    @Test(dependsOnMethods = {"saveSiteLog"})
    @Rollback(false)
    public void checkSite() throws Exception {
        IgsSiteLog siteLog = siteLogs.findByFourCharacterId(fourCharId);
        CorsSite site = sites.findByFourCharacterId(fourCharId);
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
