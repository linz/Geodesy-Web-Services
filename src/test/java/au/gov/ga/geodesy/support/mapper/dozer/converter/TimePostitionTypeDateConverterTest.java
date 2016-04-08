package au.gov.ga.geodesy.support.mapper.dozer.converter;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePostitionTypeDateConverter;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePositionType;

public class TimePostitionTypeDateConverterTest {
    private Date instantDate;

    TimePostitionTypeDateConverter conv = new TimePostitionTypeDateConverter();

    public TimePostitionTypeDateConverterTest() {
        instantDate = new Date();
    }

    public Date getInstantDate() {
        return instantDate;
    }

    /**
     * source: String
     * dest: TimePositionType (TimeInstantType)
     * 
     * @throws ParseException
     */
    @Test
    public void test1() throws ParseException {
        Date in = GMLDateUtils.stringToDateMultiParsers("2011-07-20"); // correct format
        TimePositionType out = null;
        String expected = "2011-07-20T00:00:00Z";

        out = (TimePositionType) conv.convert(out, in, TimePositionType.class, String.class);

        Assert.assertEquals(expected, out.getValue().get(0));
    }

    @Test
    public void test2() throws ParseException {
        Date in = GMLDateUtils.stringToDateMultiParsers("2011-20-07"); // in-correct format
        TimePositionType out = null;
      String expected = "2011-07-20T00:00:00Z";

        out = (TimePositionType) conv.convert(out, in, TimePositionType.class, String.class);

        Assert.assertEquals(expected, out.getValue().get(0));
    }

    @Test
    public void test3() throws ParseException {
        Date in = GMLDateUtils.stringToDateMultiParsers("2011-07-20T16:00Z");
        TimePositionType out = null;
      String expected = "2011-07-20T16:00:00Z";

        out = (TimePositionType) conv.convert(out, in, TimePositionType.class, String.class);

        Assert.assertEquals(expected, out.getValue().get(0));
    }

    /*
     * source: TimePositionType (TimeInstantType)
     * dest: Date
     */
    @Test
    public void test2_1() {
        TimePositionType in = getGoedMLTimePrimitive();
        Date out = null;
        String expected = GMLDateUtils.dateToString(getInstantDate(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_OUTPUT);

        out = (Date) conv.convert(out, in, TimePositionType.class, Date.class);

        String dateOut = GMLDateUtils.dateToString(out, GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_OUTPUT);

        Assert.assertEquals(expected, dateOut);
    }

    /* ****************************************** */
    private TimePositionType getGoedMLTimePrimitive() {
        TimePositionType tpt = new TimePositionType();
        tpt.getValue().add(GMLDateUtils.dateToString(getInstantDate(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_SEC));

        return tpt;
    }
}
