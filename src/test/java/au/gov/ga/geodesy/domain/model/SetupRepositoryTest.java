package au.gov.ga.geodesy.domain.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.IgsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.RepositoryTest;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;

@ContextConfiguration(
        classes = {GeodesyServiceTestConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class SetupRepositoryTest extends RepositoryTest {

    @Autowired
    private SetupRepository setups;

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private IgsSiteLogService siteLogService;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog alice = new SopacSiteLogReader(TestResources.sopacSiteLogReader("ALIC")).getSiteLog();
        siteLogService.upload(alice);
    }

    @Test(dependsOnMethods = {"upload"})
    public void testFindByDateRange() {
        Integer siteId = sites.findByFourCharacterId("ALIC").getId();
        Instant effectiveFrom = GMLDateUtils.stringToDate("2001-12-12", "uuuu-MM-dd");
        Instant effectiveTo = GMLDateUtils.stringToDate("2014-05-05", "uuuu-MM-dd");
        List<Setup> result = setups.findBySiteIdAndDateRange(siteId, effectiveFrom, effectiveTo);

        EffectiveDates firstPeriod = result.get(0).getEffectivePeriod();
        assertEquals(GMLDateUtils.dateToString(firstPeriod.getFrom(), "uuuu-MM-dd"), "2000-01-24");

        EffectiveDates lastPeriod = result.get(result.size() - 1).getEffectivePeriod();
        assertNull(lastPeriod.getTo());
    }
}
