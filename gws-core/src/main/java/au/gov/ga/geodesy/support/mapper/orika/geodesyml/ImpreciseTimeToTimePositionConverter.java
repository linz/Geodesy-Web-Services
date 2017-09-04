package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Joiner;

import au.gov.ga.geodesy.support.java.util.ImpreciseTime;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import net.opengis.gml.v_3_2_1.TimePositionType;

// TODO: swap type parameters, DTO first
public class ImpreciseTimeToTimePositionConverter extends BidirectionalConverter<ImpreciseTime, TimePositionType> {

    private static final ZoneId UTC = ZoneId.of("UTC");

    private HashMap<ImpreciseTime.Field, String[]> timePatterns = new HashMap<>();

    public ImpreciseTimeToTimePositionConverter() {
        timePatterns.put(ImpreciseTime.Field.TIME_OF_DAY, new String[] {
            "uuuu-MM-dd'T'HH:mm:ss.SSSZ",
            "uuuu-MM-dd'T'HH:mm:ssX",
            "uuuu-MM-dd'T'HH:mmX",
        });
        timePatterns.put(ImpreciseTime.Field.DAY, new String[] {
            "uuuu-MM-dd",
            "uuuu-MM-ddX",
        });
        timePatterns.put(ImpreciseTime.Field.MONTH, new String[] {
            "uuuu-MM",
        });
        timePatterns.put(ImpreciseTime.Field.YEAR, new String[] {
            "uuuu",
        });
    }

    private DateTimeFormatter dateFormat(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
    }

    public TimePositionType convertTo(ImpreciseTime date, Type<TimePositionType> targetType, MappingContext ctx) {
        TimePositionType time = new TimePositionType();
        time.getValue().add(date.toString());
        return time;
    }

    public ImpreciseTime convertFrom(TimePositionType time, Type<ImpreciseTime> targetType, MappingContext ctx) {
        if (time.getValue().isEmpty()) {
            return null;
        }
        final String dateString = time.getValue().get(0);

        List<String> attemptedPatterns = new ArrayList<>();

        Optional<ImpreciseTime> result;

        result = Arrays.stream(timePatterns.get(ImpreciseTime.Field.TIME_OF_DAY))
            .map(pattern -> {
                attemptedPatterns.add(pattern);
                Instant instant = null;
                try {
                    instant = OffsetDateTime.parse(dateString, dateFormat(pattern).withZone(UTC)).toInstant();
                }
                catch (DateTimeParseException e) {
                }
                if (instant != null) {
                    return Optional.of(new ImpreciseTime(instant, ImpreciseTime.Field.TIME_OF_DAY));
                } else {
                    Optional<ImpreciseTime> empty = Optional.empty(); // force target-type inference
                    return empty;
                }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();

        if (result.isPresent()) {
            return result.get();
        }

        result = Arrays.stream(timePatterns.get(ImpreciseTime.Field.DAY))
            .map(pattern -> {
                attemptedPatterns.add(pattern);
                LocalDate date = null;
                try {
                    date = LocalDate.parse(dateString, dateFormat(pattern));
                } catch (DateTimeParseException e) {
                }
                if (date != null) {
                    return Optional.of(
                            new ImpreciseTime(date.atStartOfDay(ZoneId.of("UTC")).toInstant(),
                            ImpreciseTime.Field.DAY));
                } else {
                    Optional<ImpreciseTime> empty = Optional.empty(); // force target-type inference
                    return empty;
                }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();

        if (result.isPresent()) {
            return result.get();
        }

        result = Arrays.stream(timePatterns.get(ImpreciseTime.Field.MONTH))
            .map(yearMonthPattern -> {
                attemptedPatterns.add(yearMonthPattern);
                YearMonth yearMonth = null;
                try {
                    yearMonth = YearMonth.parse(dateString, dateFormat(yearMonthPattern));
                } catch (DateTimeParseException e) {
                }
                if (yearMonth != null) {
                    return Optional.of(
                            new ImpreciseTime(yearMonth.atDay(1).atStartOfDay(ZoneId.of("UTC")).toInstant(),
                            ImpreciseTime.Field.MONTH));
                } else {
                    Optional<ImpreciseTime> empty = Optional.empty(); // force target-type inference
                    return empty;
                }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();

        if (result.isPresent()) {
            return result.get();
        }

        result = Arrays.stream(timePatterns.get(ImpreciseTime.Field.YEAR))
            .map(yearPattern -> {
                attemptedPatterns.add(yearPattern);
                Year year = null;
                try {
                    year = Year.parse(dateString, dateFormat(yearPattern));
                } catch (DateTimeParseException e) {
                }
                if (year != null) {
                    return Optional.of(
                            new ImpreciseTime(year.atDay(1).atStartOfDay(ZoneId.of("UTC")).toInstant(),
                            ImpreciseTime.Field.YEAR));
                } else {
                    Optional<ImpreciseTime> empty = Optional.empty(); // force target-type inference
                    return empty;
                }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();

        if (result.isPresent()) {
            return result.get();
        }

        throw new RuntimeException("Failed to parse " + dateString + " using the following patterns: " +
                Joiner.on(", ").join(attemptedPatterns));
    }
}
