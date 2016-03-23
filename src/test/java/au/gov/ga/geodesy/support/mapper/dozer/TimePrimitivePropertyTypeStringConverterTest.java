package au.gov.ga.geodesy.support.mapper.dozer;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.mapper.dozer.TimePrimitivePropertyTypeStringConverter;
import au.gov.ga.geodesy.support.mapper.dozer.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;

public class TimePrimitivePropertyTypeStringConverterTest {
    private Date instantDate;

    TimePrimitivePropertyTypeStringConverter conv = new TimePrimitivePropertyTypeStringConverter();

    public TimePrimitivePropertyTypeStringConverterTest() {
        instantDate = new Date();
    }

    public Date getInstantDate() {
        return instantDate;
    }

    /**
     * source: String
     * dest: TimePrimitivePropertyType (TimeInstantType)
     */
    @Test
    public void test1() {
        String in = "2011-07-20"; // correct format
        TimePrimitivePropertyType out = null;
        String expected = "20 Jul 2011";

        out = (TimePrimitivePropertyType) conv.convert(out, in, TimePrimitivePropertyType.class, String.class);

        Assert.assertEquals(expected,
                TimePrimitivePropertyTypeUtils.getTheTimeInstantType(out).getTimePosition().getValue().get(0));
    }

    @Test
    public void test2() {
        String in = "2011-20-07"; // in-correct format
        TimePrimitivePropertyType out = null;
        String expected = "20 Jul 2011";

        out = (TimePrimitivePropertyType) conv.convert(out, in, TimePrimitivePropertyType.class, String.class);

        Assert.assertEquals(expected,
                TimePrimitivePropertyTypeUtils.getTheTimeInstantType(out).getTimePosition().getValue().get(0));
    }

    @Test
    public void test3() {
        String in = "2011-07-20T16:00Z";
        TimePrimitivePropertyType out = null;
        String expected = "20 Jul 2011 16:00 GMT";

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
        String expected = GMLDateUtils.dateToString(getInstantDate(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_OUTPUT);

        out = (String) conv.convert(out, in, TimePrimitivePropertyType.class, String.class);

        Assert.assertEquals(expected, out);
    }

    /* ****************************************** */
    private TimePrimitivePropertyType getGoedMLTimePrimitive() {
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils
                .addTimeInstantType(TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(null));
        TimePrimitivePropertyTypeUtils.getTheTimeInstantType(timePrimitivePropertyType)
                .setTimePosition(TimePrimitivePropertyTypeUtils.buildTimePositionType(getInstantDate()));

        return timePrimitivePropertyType;
    }
}
