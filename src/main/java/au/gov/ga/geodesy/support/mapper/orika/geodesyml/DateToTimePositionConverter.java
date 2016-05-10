package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import net.opengis.gml.v_3_2_1.TimePositionType;

public class DateToTimePositionConverter extends BidirectionalConverter<Date, TimePositionType> {

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static final String defaultTimePositionPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final String[] timePositionPatterns = {
        defaultTimePositionPattern,
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-ddX",
    };

    private FastDateFormat dateFormat(String pattern) {
        return FastDateFormat.getInstance(pattern, UTC);
    }

    public TimePositionType convertTo(Date date, Type<TimePositionType> targetType, MappingContext ctx) {
        TimePositionType time = new TimePositionType();
        time.getValue().add(dateFormat(defaultTimePositionPattern).format(date));
        return time;
    }

    public Date convertFrom(TimePositionType time, Type<Date> targetType, MappingContext ctx) {
        if (time.getValue().isEmpty()) {
            return null;
        }
        final String dateString = time.getValue().get(0);
        Optional<Date> date = Arrays.stream(timePositionPatterns)
            .map(pattern -> {
                    try {
                        return Optional.of(dateFormat(pattern).parse(dateString));
                    }
                    catch (ParseException e) {
                        Optional<Date> empty = Optional.empty(); // force target-type inference
                        return empty;
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
