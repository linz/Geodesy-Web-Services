package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import net.opengis.gml.v_3_2_1.TimePositionType;

// TODO: swap type parameters, DTO first
public class InstantToTimePositionConverter extends BidirectionalConverter<Instant, TimePositionType> {

    private static final ZoneId UTC = ZoneId.of("UTC");

    private static final String defaultTimePositionPattern = "uuuu-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final String[] timePositionPatterns = {
        defaultTimePositionPattern,
        "uuuu-MM-dd'T'HH:mm:ss.SSSZ",
        "uuuu-MM-dd'T'HH:mm:ssX",
        "uuuu-MM-ddX",
        "uuuu-MM-dd"
    };

    private DateTimeFormatter dateFormat(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT).withZone(UTC);
    }

    public TimePositionType convertTo(Instant date, Type<TimePositionType> targetType, MappingContext ctx) {
        TimePositionType time = new TimePositionType();
        time.getValue().add(dateFormat(defaultTimePositionPattern).format(date));
        return time;
    }

    public Instant convertFrom(TimePositionType time, Type<Instant> targetType, MappingContext ctx) {
        if (time.getValue().isEmpty()) {
            return null;
        }
        final String dateString = time.getValue().get(0);
        Optional<Instant> date = Arrays.stream(timePositionPatterns)
            .map(pattern -> {
                    try {
                        return Optional.of(
                                OffsetDateTime.parse(dateString, dateFormat(pattern)).toInstant());
                    }
                    catch (DateTimeParseException e) {
                        try {
                            return Optional.of(
                                    LocalDate.parse(dateString, dateFormat(pattern)).atStartOfDay(ZoneId.of("UTC")).toInstant());
                        }
                        catch (DateTimeParseException e2) {
                            Optional<Instant> empty = Optional.empty(); // force target-type inference
                            return empty;
                        }
                    }
                }
            )
            .filter(Optional::isPresent).map(Optional::get).findFirst();
        if (date.isPresent()) {
            return date.get();
        } else {
            String attemptedPatterns = StringUtils.join(timePositionPatterns, ", ");
            throw new RuntimeException("Failed to parse " + dateString + " using the following patterns: " + attemptedPatterns);
        }
    }
}
