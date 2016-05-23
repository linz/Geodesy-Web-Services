package au.gov.ga.geodesy.domain.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.IgsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {GeodesyServiceTestConfig.class, GeodesySupportConfig.class, PersistenceJpaConfig.class },
        loader = AnnotationConfigContextLoader.class)

@Transactional("geodesyTransactionManager")
public class SetupRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

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
        Date effectiveFrom = parseDate("yyyy-MM-dd", "2001-12-12");
        Date effectiveTo = parseDate("yyyy-MM-dd", "2014-05-05");
        List<Setup> result = setups.findBySiteIdAndDateRange(siteId, effectiveFrom, effectiveTo);

        EffectiveDates firstPeriod = result.get(0).getEffectivePeriod();
        assertEquals(formatDate("yyyy-MM-dd", firstPeriod.getFrom()), "2000-01-24");

        EffectiveDates lastPeriod = result.get(result.size() - 1).getEffectivePeriod();
        assertNull(lastPeriod.getTo());
    }

    private Date parseDate(String pattern, String str) {
        try {
            return FastDateFormat.getInstance(pattern, TimeZone.getTimeZone("UTC")).parse(str);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatDate(String pattern, Date date) {
        return FastDateFormat.getInstance(pattern, TimeZone.getTimeZone("UTC")).format(date);
    }
}
