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

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.CorsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.RepositoryTest;
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
    private CorsSiteLogService siteLogService;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog alice = new SopacSiteLogReader(TestResources.sopacSiteLogReader("ALIC")).getSiteLog();
        siteLogService.upload(alice);
    }

    // TODO: remove, if can be superseded by query dsl
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

    @Test(dependsOnMethods = {"upload"})
    public void testFindByDateRangeUsingQueryDsl() {
        Integer siteId = sites.findByFourCharacterId("ALIC").getId();
        Instant effectiveFrom = GMLDateUtils.stringToDate("2001-12-12", "uuuu-MM-dd");
        Instant effectiveTo = GMLDateUtils.stringToDate("2014-05-05", "uuuu-MM-dd");

        QSetup setup = QSetup.setup;
        BooleanExpression isValid = setup.invalidated.isFalse();

        BooleanExpression isContained =
            setup.effectivePeriod.from.goe(effectiveFrom)
            .and(setup.effectivePeriod.to.loe(effectiveTo));

        BooleanExpression isInterceptedByFromDate =
            setup.effectivePeriod.from.loe(effectiveFrom)
            .and(setup.effectivePeriod.to.goe(effectiveFrom));

        BooleanExpression isInterceptedByToDate =
            setup.effectivePeriod.from.loe(effectiveTo)
            .and(setup.effectivePeriod.to.goe(effectiveTo).or(setup.effectivePeriod.to.isNull()));

        BooleanExpression isIntercepted = isInterceptedByFromDate.or(isInterceptedByToDate);

        List<Setup> result = Lists.newArrayList(setups.findAll(setup
            .siteId.eq(siteId)
            .and(isValid)
            .and(isContained.or(isIntercepted))
        ));

        EffectiveDates firstPeriod = result.get(0).getEffectivePeriod();
        assertEquals(GMLDateUtils.dateToString(firstPeriod.getFrom(), "uuuu-MM-dd"), "2000-01-24");

        EffectiveDates lastPeriod = result.get(result.size() - 1).getEffectivePeriod();
        assertNull(lastPeriod.getTo());
    }
}
