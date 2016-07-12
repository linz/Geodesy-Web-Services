package au.gov.ga.geodesy.support.mapper.dozer.converter;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePositionType;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TimePositionTypeInstantConverterTest {
    private Instant instantDate = Instant.now();

    TimePositionTypeInstantConverter conv = new TimePositionTypeInstantConverter();

    /**
     * source: String
     * dest: TimePositionType (TimeInstantType)
     *
     * @throws ParseException
     */
    @Test
    public void test1() throws ParseException {
        Instant in = GMLDateUtils.stringToDateMultiParsers("2011-07-20"); // correct format
        TimePositionType out = null;
        String expected = "2011-07-20T00:00:00Z";
        out = (TimePositionType) conv.convert(out, in, TimePositionType.class, String.class);
        assertThat(out.getValue().get(0), is(expected));
    }

    @Test(expectedExceptions = GeodesyRuntimeException.class)
    public void test2() throws ParseException {
        Instant in = GMLDateUtils.stringToDateMultiParsers("2011-20-07"); // in-correct format
    }

    @Test
    public void test3() throws ParseException {
        Instant in = GMLDateUtils.stringToDateMultiParsers("2011-07-20T16:00Z");
        TimePositionType out = null;
        String expected = "2011-07-20T16:00:00Z";
        out = (TimePositionType) conv.convert(out, in, TimePositionType.class, String.class);
        assertThat(out.getValue().get(0), is(expected));
    }

    /*
     * source: TimePositionType (TimeInstantType)
     * dest: Date
     */
    @Test
    public void test2_1() {
        TimePositionType in = getGoedMLTimePrimitive();
        Instant out = null;
        String expected = GMLDateUtils.dateToString(instantDate, GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_OUTPUT);
        out = (Instant) conv.convert(out, in, TimePositionType.class, Instant.class);
        String dateOut = GMLDateUtils.dateToString(out, GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_OUTPUT);
        assertThat(dateOut, is(expected));
    }

    @Test(expectedExceptions = GeodesyRuntimeException.class)
    public void testBadFormat() {
        Instant in = GMLDateUtils.stringToDateMultiParsers("2008-03-26T00");
    }

    /* ****************************************** */
    private TimePositionType getGoedMLTimePrimitive() {
        TimePositionType tpt = new TimePositionType();
        tpt.getValue().add(GMLDateUtils.dateToString(instantDate, GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_SEC));
        return tpt;
    }
}
