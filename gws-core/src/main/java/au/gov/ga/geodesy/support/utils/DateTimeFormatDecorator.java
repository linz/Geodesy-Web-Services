package au.gov.ga.geodesy.support.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DateTimeFormatDecorator {

    private static final Map<DateTimeFormatter, String> patternHistory = new HashMap<DateTimeFormatter, String>();

    public static DateTimeFormatter ofPattern(String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
                .withResolverStyle(ResolverStyle.STRICT)
                .withZone(ZoneId.of("UTC"));
        patternHistory.put(dateTimeFormatter, pattern);
        return dateTimeFormatter;
    }

    public static String getPattern(DateTimeFormatter dateTimeFormatter) {
        return patternHistory.get(dateTimeFormatter);
    }
}