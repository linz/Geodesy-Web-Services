package au.gov.ga.geodesy.support.mapper.dozer.converter;

import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.junit.Assert.*;

/**
 * Created by brookes on 14/06/16.
 */
public class TimePrimitivePropertyTypeUtilsTest {
    @Test
    public void NoInstantNorPeriod() throws Exception {
        // No arg
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType();  // should be no child time (neither instant nor period)
        Assert.assertNull(timePrimitivePropertyType.getAbstractTimePrimitive());
    }

    @Test
    public void TwoNullArgs_NoInstantNorPeriod() throws Exception {
        // No arg
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(null, null);  // should be no child time (neither instant nor period)
        Assert.assertNull(timePrimitivePropertyType.getAbstractTimePrimitive());
    }

    @Test
    public void OneArg_Instant() throws Exception {
        // One arg - should construct an Instant
        Instant now = Instant.now();

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(now);
        TimeInstantType tit = TimePrimitivePropertyTypeUtils.getTheTimeInstantType(timePrimitivePropertyType);
        Assert.assertNotNull(tit);
        Assert.assertNotNull(tit.getTimePosition());
        Assert.assertNotNull(tit.getTimePosition().getValue());
        Assert.assertEquals(1, tit.getTimePosition().getValue().size());
    }

    @Test
    public void TwoArgs_Period() throws Exception {
        // Two args - should construct a Period with begin and end
        Instant end = Instant.now();
        Instant begin = end.minus(1, ChronoUnit.DAYS);

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(begin, end);
        TimePeriodType tpt = TimePrimitivePropertyTypeUtils.getTheTimePeriodType(timePrimitivePropertyType);
        Assert.assertNotNull(tpt);
        // Now test for begin and end
        Assert.assertNotNull(tpt.getBeginPosition());
        Assert.assertNotNull(tpt.getBeginPosition().getValue());
        Assert.assertEquals(1, tpt.getBeginPosition().getValue().size());
        Assert.assertNotNull(tpt.getEndPosition());
        Assert.assertNotNull(tpt.getEndPosition().getValue());
        Assert.assertEquals(1, tpt.getEndPosition().getValue().size());
    }

    @Test
    public void OneArgBegin_Period() throws Exception {
        // Two args - first only, null for second - should construct a Period with null end
        Instant begin = Instant.now();

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(begin, null);
        TimePeriodType tpt = TimePrimitivePropertyTypeUtils.getTheTimePeriodType(timePrimitivePropertyType);
        Assert.assertNotNull(tpt);
        // Now test for begin and end
        Assert.assertNotNull(tpt.getBeginPosition());
        Assert.assertNotNull(tpt.getBeginPosition().getValue());
        Assert.assertEquals(1, tpt.getBeginPosition().getValue().size());
        Assert.assertNull(tpt.getEndPosition());
    }

    @Test
    public void oneArgEnd_Period() throws Exception {
        // Two args - second only, null for first - should construct a Period with null begin
        Instant end = Instant.now();

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(null, end);
        TimePeriodType tpt = TimePrimitivePropertyTypeUtils.getTheTimePeriodType(timePrimitivePropertyType);
        Assert.assertNotNull(tpt);
        // Now test for begin and end
        Assert.assertNull(tpt.getBeginPosition());
        Assert.assertNotNull(tpt.getEndPosition());
        Assert.assertNotNull(tpt.getEndPosition().getValue());
        Assert.assertEquals(1, tpt.getEndPosition().getValue().size());
    }
}