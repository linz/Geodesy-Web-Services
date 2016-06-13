package au.gov.ga.geodesy.support.mapper.dozer.converter;

import java.time.Instant;
import java.time.ZoneId;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;

public class TimePrimitivePropertyTypeStringConverterTest {
    private Instant instantDate = Instant.now();

    TimePrimitivePropertyTypeStringConverter conv = new TimePrimitivePropertyTypeStringConverter();

    /**
     * source: String
     * dest: TimePrimitivePropertyType (TimeInstantType)
     */
    @Test
    public void test1() {
        String in = "2011-07-20"; // correct format
        TimePrimitivePropertyType out = null;
        String expected = "2011-07-20T00:00:00.000Z";
        out = (TimePrimitivePropertyType) conv.convert(out, in, TimePrimitivePropertyType.class, String.class);
        Assert.assertEquals(expected,
                TimePrimitivePropertyTypeUtils.getTheTimeInstantType(out).getTimePosition().getValue().get(0));
    }

    @Test(expected = GeodesyRuntimeException.class)
    public void test2() {
        String in = "2011-20-07"; // in-correct format
        TimePrimitivePropertyType out = null;
        out = (TimePrimitivePropertyType) conv.convert(out, in, TimePrimitivePropertyType.class, String.class);
    }

    @Test
    public void test3() {
        String in = "2011-07-20T16:00Z";
        TimePrimitivePropertyType out = null;
        String expected = "2011-07-20T16:00:00.000Z";
        out = (TimePrimitivePropertyType) conv.convert(out, in, TimePrimitivePropertyType.class, String.class);
        Assert.assertEquals(expected,
                TimePrimitivePropertyTypeUtils.getTheTimeInstantType(out).getTimePosition().getValue().get(0));
    }

    /*
     * source: TimePrimitivePropertyType (TimeInstantType)
     * dest: String
     */
    @Test
    public void test2_1() {
        TimePrimitivePropertyType in = getGoedMLTimePrimitive();
        String out = null;
        String expected = GMLDateUtils.dateToString(instantDate, GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC);
        out = (String) conv.convert(out, in, TimePrimitivePropertyType.class, String.class);
        Assert.assertEquals(expected, out);
    }

    /* ****************************************** */
    private TimePrimitivePropertyType getGoedMLTimePrimitive() {
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils
                .addTimeInstantType(TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(null));
        TimePrimitivePropertyTypeUtils.getTheTimeInstantType(timePrimitivePropertyType)
                .setTimePosition(TimePrimitivePropertyTypeUtils.buildTimePositionType(instantDate));
        return timePrimitivePropertyType;
    }
}
