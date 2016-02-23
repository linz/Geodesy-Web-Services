package au.gov.ga.geodesy.support.utils;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;

/**
 * Date formast have observed in input data:
 * 1992-08-12
 * 2011-20-07 (wrong way around)
 * 1994-05-15T00:00Z
 * 
 **/
public class GMLDateUtilsTest {

    @Test
    public void testMultiFormats01() {
        String in = "1992-08-12";
        String expected = "12 Aug 1992";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
        System.out.println("In date: " + in + ", out date: " + out);
        Assert.assertEquals(expected, out);
    }

    @Test
    public void testMultiFormats012() {
        String in = "2011-20-07";
        String expected = "20 Jul 2011";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
        System.out.println("In date: " + in + ", out date: " + out);
        Assert.assertEquals(expected, out);
    }

    @Test
    public void testMultiFormats013() {
        String in = "1994-05-15T00:00Z";
        String expected = "15 May 1994 00:00 GMT";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
        System.out.println("In date: " + in + ", out date: " + out);
        Assert.assertEquals(expected, out);
    }

    @Test(expected = GeodesyRuntimeException.class)
    public void testMultiFormatsUnacceptedFormat() {
        String in = "15 07 1994";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
    }
}
