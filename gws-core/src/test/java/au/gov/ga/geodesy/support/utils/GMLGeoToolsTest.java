package au.gov.ga.geodesy.support.utils;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;

/**
 * Using https://data.aad.gov.au/aadc/calc/dms_decimal.cfm to get the expected results.
 *
 * @author brookes
 */
public class GMLGeoToolsTest {
    @Test
    public void firstDevelopingAlgo() {
        long significantPart = 654;
        String sigPartString = Long.toString(significantPart);
        assertThat(sigPartString.length(), lessThanOrEqualTo(6));
        // String sigPartTrailing = sigPartString + "00000000";
        String formatted = (sigPartString + "00000000").substring(0, 6);// sigPartString.length());// String.format("%d06",
        // significantPart);
        assertThat(formatted, is("654000"));

        String[] dmsParts = new String[]{formatted.substring(0, 2), formatted.substring(2, 4), formatted.substring(4, 6)};
        // System.out.println(String.format("dmsParts: %s,%s,%s", dmsParts[0], dmsParts[1], dmsParts[2]));
        assertThat(dmsParts[0], is("65"));
        assertThat(dmsParts[1], is("40"));
        assertThat(dmsParts[2], is("00"));
    }

    @Test
    public void testDMSConvert01() {
        Double in = new Double(-234012);
        double expected = -23.67;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert012() {
        Double in = new Double(234012);
        double expected = 23.67;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert02() {
        Double in = new Double(-234012.5);
        double expected = -23.670139;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert022() {
        Double in = new Double(234012.5);
        double expected = 23.670139;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert03() {
        Double in = new Double(-1465455.99);
        double expected = -146.915553;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert032() {
        Double in = new Double(+1465455.99);
        double expected = 146.915553;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert04() {
        Double in = new Double(-1335307.84759);
        double expected = -133.885513;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert042() {
        Double in = new Double(+1335307.84759);
        double expected = 133.885513;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert05() {
        Double in = new Double(-234012.44594);
        double expected = -23.670124;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert052() {
        Double in = new Double(+234012.44594);
        double expected = 23.670124;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert06() {
        Double in = new Double(-0072754.36);
        double expected = -7.465100;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

    @Test
    public void testDMSConvert062() {
        Double in = new Double(+0072754.36);
        double expected = 7.465100;
        assertThat(GMLGeoTools.dmsToDecmial(in), closeTo(expected, 0.001));
    }

}
