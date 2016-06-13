package au.gov.ga.geodesy.support.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;

/**
 * Date formast have observed in input data:
 * 1992-08-12
 * 2011-20-07 (wrong way around)
 * 1994-05-15T00:00Z
 **/
public class GMLDateUtilsTest {

    @Test
    public void testParser() {
        Instant dateTime = Instant.parse("2000-01-12T13:00:00Z");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX")
                .withZone(ZoneId.of("UTC"));
        Assert.assertEquals("2000-01-12T13:00:00Z", formatter.format(dateTime));

        formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX").withResolverStyle(ResolverStyle.STRICT)
                .withZone(ZoneId.of("UTC"));

        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2009-10-22T13:30:00Z", formatter);
        dateTime = offsetDateTime.toInstant();
        Assert.assertEquals("2009-10-22T13:30:00Z", formatter.format(dateTime));


        formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
                .withZone(ZoneId.of("UTC"));
        LocalDate date = LocalDate.parse("2000-08-12", formatter);
        dateTime = date.atStartOfDay(ZoneId.of("UTC")).toInstant();

        formatter = DateTimeFormatter.ofPattern("dd MMM uuuu HH:mm z")
                .withZone(ZoneId.of("UTC"));

        Assert.assertEquals("12 Aug 2000 00:00 UTC", formatter.format(dateTime));
    }

    @Test
    public void testMultiFormats01() {
        String in = "1992-08-12";
        String expected = "1992-08-12T00:00:00.000Z";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
        System.out.println("In date: " + in + ", out date: " + out);
        Assert.assertEquals(expected, out);
    }

    @Test(expected = GeodesyRuntimeException.class)
    public void testMultiFormats012() {
        String in = "2011-20-07";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
    }

    @Test
    public void testMultiFormats013() {
        String in = "1994-05-15T00:00Z";
        String expected = "1994-05-15T00:00:00.000Z";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
        System.out.println("In date: " + in + ", out date: " + out);
        Assert.assertEquals(expected, out);
    }

    @Test(expected = GeodesyRuntimeException.class)
    public void testMultiFormatsUnacceptedFormat() {
        String in = "15 07 1994";
        String out = GMLDateUtils.stringToDateToStringMultiParsers(in);
    }

    @Test
    public void testStringToDateToString01() {
        String in = "2015-06-12T06:20:08.000Z";
        String out = GMLDateUtils.stringToDateToString(in);

        Assert.assertNotNull(out);
    }

    @Test
    public void testStringToDate01() {
        String in = "2015-06-12T06:20:08.000Z";
        Instant out = GMLDateUtils.stringToDate(in);

        Assert.assertNotNull(out);
    }
}
