package au.gov.ga.geodesy.support.java.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.testng.Assert;
import org.testng.annotations.Test;

public class IsoTest {

    private class StringToDate implements Iso<String, Date> {

        private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'hh:mm:ss.SSS");

        public Date to(String str) {
            try {
                return dateFormat.parse(str);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public String from(Date date) {
            return dateFormat.format(date);
        }
    }

    private class DateToLong implements Iso<Date, Long> {
        public Long to(Date date) {
            return date.getTime();
        }

        public Date from(Long n) {
            return new Date(n);
        }
    }

    @Test
    public void testComposition() {
        Iso<String, Long> stringToLong = new StringToDate().compose(new DateToLong());
        Assert.assertTrue(stringToLong.to(stringToLong.from(1L)) == 1L);
    }
}
