package au.gov.ga.geodesy.support.utils;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertTrue(sigPartString.length() <= 6);
        // String sigPartTrailing = sigPartString + "00000000";
        String formatted = (sigPartString + "00000000").substring(0, 6);// sigPartString.length());// String.format("%d06",
                                                                        // significantPart);
        Assert.assertEquals("654000", formatted);

        String[] dmsParts = new String[] {formatted.substring(0, 2), formatted.substring(2, 4),
                formatted.substring(4, 6)};
        // System.out.println(String.format("dmsParts: %s,%s,%s", dmsParts[0], dmsParts[1], dmsParts[2]));
        Assert.assertEquals("65", dmsParts[0]);
        Assert.assertEquals("40", dmsParts[1]);
        Assert.assertEquals("00", dmsParts[2]);
    }

    @Test
    public void testDMSConvert01() {
        Double in = new Double(-234012);
        double expected = -23.67;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.northing), 0.001);
    }

    @Test
    public void testDMSConvert012() {
        Double in = new Double(234012);
        double expected = 23.67;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.northing), 0.001);
    }

    @Test
    public void testDMSConvert02() {
        Double in = new Double(-234012.5);
        double expected = -23.670139;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.northing), 0.001);
    }

    @Test
    public void testDMSConvert022() {
        Double in = new Double(234012.5);
        double expected = 23.670139;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.northing), 0.001);
    }

    @Test
    public void testDMSConvert03() {
        Double in = new Double(-1465455.99);
        double expected = -146.915553;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.easting), 0.001);
    }

    @Test
    public void testDMSConvert032() {
        Double in = new Double(+1465455.99);
        double expected = 146.915553;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.easting), 0.001);
    }

    @Test
    public void testDMSConvert04() {
        Double in = new Double(-1335307.84759);
        double expected = -133.885513;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.easting), 0.001);
    }

    @Test
    public void testDMSConvert042() {
        Double in = new Double(+1335307.84759);
        double expected = 133.885513;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.easting), 0.001);
    }

    @Test
    public void testDMSConvert05() {
        Double in = new Double(-234012.44594);
        double expected = -23.670124;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.northing), 0.001);
    }

    @Test
    public void testDMSConvert052() {
        Double in = new Double(+234012.44594);
        double expected = 23.670124;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.northing), 0.001);
    }

    @Test
    public void testDMSConvert06() {
        Double in = new Double(-0072754.36);
        double expected = -7.465100;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.easting), 0.001);
    }

    @Test
    public void testDMSConvert062() {
        Double in = new Double(+0072754.36);
        double expected = 7.465100;
        Assert.assertEquals(expected, GMLGeoTools.dmsToDecmial(in, GMLGeoTools.easting), 0.001);
    }

}
