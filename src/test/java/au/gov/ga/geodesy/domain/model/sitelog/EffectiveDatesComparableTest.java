package au.gov.ga.geodesy.domain.model.sitelog;

import org.testng.annotations.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Tests whether a collection of effective dates can be properly sorted
 * and that both from dates and two dates are compared correctly.
 */
public class EffectiveDatesComparableTest {
    @Test
    public void testCompareFromDates() throws Exception {

        Instant now = Instant.now();

        EffectiveDates [] dates = {
                new EffectiveDates(
                        now.minusSeconds(100),
                        now.minusSeconds(90)),
                new EffectiveDates(
                        now.minusSeconds(90),
                        now.minusSeconds(80)),
                new EffectiveDates(
                        now.minusSeconds(80),
                        null),
                new EffectiveDates(
                        null,
                        null)
        };

        List<EffectiveDates> datesList = new ArrayList<EffectiveDates>();
        datesList.add(dates[2]);
        datesList.add(dates[3]);
        datesList.add(dates[0]);
        datesList.add(dates[1]);

        Collections.sort(datesList);

        int i = 0;
        for (EffectiveDates date : datesList) {
            assertEquals(dates[i++], date);
        }
    }

    @Test
    public void testCompareToDates() throws Exception {

        Instant now = Instant.now();

        EffectiveDates [] dates = {
                new EffectiveDates(
                        now.minusSeconds(100),
                        now.minusSeconds(90)),
                new EffectiveDates(
                        now.minusSeconds(100),
                        now.minusSeconds(80)),
                new EffectiveDates(
                        now.minusSeconds(100),
                        null),
                new EffectiveDates(
                        null,
                        null)
        };

        List<EffectiveDates> datesList = new ArrayList<EffectiveDates>();
        datesList.add(dates[2]);
        datesList.add(dates[3]);
        datesList.add(dates[0]);
        datesList.add(dates[1]);

        Collections.sort(datesList);

        int i = 0;
        for (EffectiveDates date : datesList) {
            assertEquals(dates[i++], date);
        }
    }

}