package au.gov.ga.geodesy.domain.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.MockEventPublisher;
import au.gov.ga.geodesy.domain.model.SiteLogReceived;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.support.spring.GeodesyServiceUnitTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesyServiceUnitTestConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class UpdateABRKTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String siteLogsDir = "src/test/resources/sitelog/";

    @Autowired
    private GnssCorsSiteService siteService;

    @Autowired
    private GnssCorsSiteRepository sites;

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    public MockEventPublisher eventPublisher;

    @Test
    @Rollback(false)
    public void saveSiteLog() throws Exception {
        File abrk = new File(siteLogsDir + "ABRK.xml");
        siteLogs.save(marshaller.unmarshal(new FileReader(abrk)));
    }

    @Test
    @Rollback(false)
    public void updateSite() throws Exception {
        siteService.handle(new SiteLogReceived("ABRK"));
    }

    @Test(dependsOnMethods = {"saveSiteLog", "updateSite"})
    @Rollback(false)
    public void checkSite() throws Exception {
        IgsSiteLog siteLog = siteLogs.findByFourCharacterId("ABRK");
        GnssCorsSite site = sites.findByFourCharacterId("ABRK");
        assertNotNull(site);
        assertEquals(site.getName(), siteLog.getSiteIdentification().getSiteName());
    }
}
