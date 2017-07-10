package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.testng.annotations.Test;

public class EffectiveDatesTest {

    @Test
    public void testInRage() {

        assertThat(
            new EffectiveDates(
                Instant.parse("2002-12-12T00:00:01.000Z"),
                Instant.parse("2002-12-12T00:00:01.000Z"))
            .inRange(Instant.parse("2002-12-12T00:00:01.000Z")), is(true));

        assertThat(
            new EffectiveDates(
                Instant.parse("2002-12-12T00:00:01.000Z"),
                Instant.parse("2002-12-12T00:00:02.000Z"))
            .inRange(Instant.parse("2002-12-12T00:00:01.000Z")), is(true));

        assertThat(
            new EffectiveDates(
                Instant.parse("2002-12-12T00:00:01.000Z"),
                Instant.parse("2002-12-12T00:00:02.000Z"))
            .inRange(Instant.parse("2002-12-12T00:00:02.000Z")), is(true));

        assertThat(
            new EffectiveDates(
                Instant.parse("2002-12-12T00:00:01.000Z"),
                Instant.parse("2002-12-12T00:00:02.000Z"))
            .inRange(Instant.parse("2002-12-12T00:00:01.001Z")), is(true));

        assertThat(
            new EffectiveDates(
                Instant.parse("2002-12-12T00:00:01.000Z"),
                Instant.parse("2002-12-12T00:00:01.000Z"))
            .inRange(Instant.parse("2002-12-12T00:00:01.001Z")), is(false));

        assertThat(
            new EffectiveDates(
                Instant.parse("2002-12-12T00:00:01.000Z"),
                null)
            .inRange(Instant.parse("2002-12-12T00:00:01.001Z")), is(true));

        assertThat(
            new EffectiveDates(
                Instant.parse("2002-12-12T00:00:01.000Z"),
                Instant.parse("2002-12-12T00:00:01.000Z"))
            .inRange(Instant.parse("2002-12-12T00:00:00.999Z")), is(false));

        assertThat(
            new EffectiveDates(
                null,
                Instant.parse("2002-12-12T00:00:01.000Z"))
            .inRange(Instant.parse("2002-12-12T00:00:00.999Z")), is(true));

        assertThat(
            new EffectiveDates(null, null).inRange(Instant.parse("2002-12-12T00:00:00.999Z")), is(true));
    }
}
