package au.gov.ga.geodesy.domain.model.sitelog;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

/**
 * Tests whether a collection of effective dates can be properly sorted
 * and that both from dates and two dates are compared correctly.
 */
public class EffectiveDatesComparableTest {
    @Test
    public void testEffectiveDatesEqualsMethod() throws Exception {

        Instant now = Instant.now();

        EffectiveDates[] dates = {new EffectiveDates(now.minusSeconds(100), now.minusSeconds(90)), new EffectiveDates(now.minusSeconds
            (90), now.minusSeconds(80)), new EffectiveDates(now.minusSeconds(80), null), new EffectiveDates(null, null)};

        List<EffectiveDates> datesList = Arrays.stream(dates).collect(Collectors.toList());

        /*
         * Confirm that every EffectiveDate in the list is equal to itself and not equal to the others.
         *
         * @param datesList
         */
        for (int index1 = 0; index1 < datesList.size(); index1++) {
            for (int j = 1; j < datesList.size(); j++) {
                int index2 = (index1 + j) % datesList.size();
                MatcherAssert.assertThat(datesList.get(index1), IsEqual.equalTo(datesList.get(index1)));
                MatcherAssert.assertThat(datesList.get(index1), IsNot.not(IsEqual.equalTo(datesList.get(index2))));
            }
        }
    }

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