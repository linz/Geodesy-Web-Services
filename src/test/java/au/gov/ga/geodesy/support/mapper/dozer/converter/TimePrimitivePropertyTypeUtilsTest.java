package au.gov.ga.geodesy.support.mapper.dozer.converter;

import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by brookes on 14/06/16.
 */
public class TimePrimitivePropertyTypeUtilsTest {
    @Test
    public void NoInstantNorPeriod() throws Exception {
        // No arg
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType();  // should
        // be no child time (neither instant nor period)
        assertThat(timePrimitivePropertyType.getAbstractTimePrimitive(), nullValue());
    }

    @Test
    public void TwoNullArgs_NoInstantNorPeriod() throws Exception {
        // No arg
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(null, null);
        // should be no child time (neither instant nor period)
        assertThat(timePrimitivePropertyType.getAbstractTimePrimitive(), nullValue());
    }

    @Test
    public void OneArg_Instant() throws Exception {
        // One arg - should construct an Instant
        Instant now = Instant.now();

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(now);
        TimeInstantType tit = TimePrimitivePropertyTypeUtils.getTheTimeInstantType(timePrimitivePropertyType);
        assertThat(tit, notNullValue());
        assertThat(tit.getTimePosition(), notNullValue());
        assertThat(tit.getTimePosition().getValue(), notNullValue());
        assertThat(tit.getTimePosition().getValue().size(), is(1));
    }

    @Test
    public void TwoArgs_Period() throws Exception {
        // Two args - should construct a Period with begin and end
        Instant end = Instant.now();
        Instant begin = end.minus(1, ChronoUnit.DAYS);

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(begin, end);
        TimePeriodType tpt = TimePrimitivePropertyTypeUtils.getTheTimePeriodType(timePrimitivePropertyType);
        assertThat(tpt, notNullValue());
        // Now test for begin and end
        assertThat(tpt.getBeginPosition(), notNullValue());
        assertThat(tpt.getBeginPosition().getValue(), notNullValue());
        assertThat(tpt.getBeginPosition().getValue().size(), is(1));
        assertThat(tpt.getEndPosition(), notNullValue());
        assertThat(tpt.getEndPosition().getValue(), notNullValue());
        assertThat(tpt.getEndPosition().getValue().size(), is(1));
    }

    @Test
    public void OneArgBegin_Period() throws Exception {
        // Two args - first only, null for second - should construct a Period with null end
        Instant begin = Instant.now();

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(begin, null);
        TimePeriodType tpt = TimePrimitivePropertyTypeUtils.getTheTimePeriodType(timePrimitivePropertyType);
        assertThat(tpt, notNullValue());
        // Now test for begin and end
        assertThat(tpt.getBeginPosition(), notNullValue());
        assertThat(tpt.getBeginPosition().getValue(), notNullValue());
        assertThat(tpt.getBeginPosition().getValue().size(), is(1));
        assertThat(tpt.getEndPosition(), nullValue());
    }

    @Test
    public void oneArgEnd_Period() throws Exception {
        // Two args - second only, null for first - should construct a Period with null begin
        Instant end = Instant.now();

        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(null, end);
        TimePeriodType tpt = TimePrimitivePropertyTypeUtils.getTheTimePeriodType(timePrimitivePropertyType);
        assertThat(tpt, notNullValue());
        // Now test for begin and end
        assertThat(tpt.getBeginPosition(), nullValue());
        assertThat(tpt.getEndPosition(), notNullValue());
        assertThat(tpt.getEndPosition().getValue(), notNullValue());
        assertThat(tpt.getEndPosition().getValue().size(), is(1));
    }
}