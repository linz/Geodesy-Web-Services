package au.gov.ga.geodesy.support.java.util;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.testng.Assert;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;

public class IsoTest {

    private class StringToDate implements Iso<String, Instant> {

        public Instant to(String str) throws DateTimeParseException {
            return GMLDateUtils.stringToDate(str, "uuuu-MM-dd'T'HH:mm:ss.SSX");
        }

        public String from(Instant date) {
            return GMLDateUtils.dateToString(date, "uuuu-MM-dd'T'HH:mm:ss.SSX");
        }
    }

    private class DateToLong implements Iso<Instant, Long> {
        public Long to(Instant date) {
            return date.getEpochSecond();
        }

        public Instant from(Long n) {
            return Instant.ofEpochSecond(n);
        }
    }

    @Test
    public void testComposition() {
        Iso<String, Long> stringToLong = new StringToDate().compose(new DateToLong());
        Assert.assertTrue(stringToLong.to(stringToLong.from(1L)) == 1L);
    }
}
