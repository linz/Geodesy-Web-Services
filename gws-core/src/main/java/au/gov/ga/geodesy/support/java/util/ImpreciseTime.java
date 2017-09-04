package au.gov.ga.geodesy.support.java.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Reduced precision time.
 */
@Embeddable
public class ImpreciseTime implements Comparable<ImpreciseTime> {

    private static final ZoneId UTC = ZoneId.of("UTC");

    public static enum Field {
        YEAR("uuuu"),
        MONTH("uuuu-MM"),
        DAY("uuuu-MM-dd"),
        TIME_OF_DAY("uuuu-MM-dd'T'HH:mm:ssXXX");

        public String format;

        private Field(String format) {
            this.format = format;
        }
    }

    private Instant time;
    @Transient
    private Instant after;
    private Field precision;

    @SuppressWarnings("unused") // used by hibernate
    private ImpreciseTime() {
    }

    public ImpreciseTime(Instant instant) {
        this(instant, Field.TIME_OF_DAY);
    }

    public ImpreciseTime(Instant instant, Field precision) {
        this.precision = precision;

        if (precision == Field.YEAR) {
            this.time = instant;
                // .with(ChronoField.DAY_OF_YEAR, 1)
                // .with(ChronoField.NANO_OF_SECOND, 0);

            // this.after = this.time.plus(1, ChronoUnit.YEARS);
        } else if (precision == Field.MONTH) {
            this.time = instant;
                // .with(ChronoField.DAY_OF_MONTH, 1)
                // .with(ChronoField.SECOND_OF_DAY, 0)
                // .with(ChronoField.NANO_OF_SECOND, 0);

            // this.after = this.time.plus(1, ChronoUnit.MONTHS);
        } else if (precision == Field.DAY) {
            this.time = instant;
                // .with(ChronoField.SECOND_OF_DAY, 0)
                // .with(ChronoField.NANO_OF_SECOND, 0);

            // this.after = this.time.plus(1, ChronoUnit.DAYS);
        } else {
            this.time = instant;
                // .with(ChronoField.NANO_OF_SECOND, 0);

            // this.after = this.time.plus(1, ChronoUnit.SECONDS);
        }
    }

    public String toString() {
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(precision.format).withResolverStyle(ResolverStyle.STRICT).withZone(UTC);

        return formatter.format(time);
    }

    public Instant getTime() {
        return time;
    }

    public Field getPrecision() {
        return precision;
    }

    public int compareTo(ImpreciseTime other) {
        return 0;
    }
}
