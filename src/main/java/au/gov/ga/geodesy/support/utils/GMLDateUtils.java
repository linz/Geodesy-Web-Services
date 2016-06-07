package au.gov.ga.geodesy.support.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;

/**
 * Define Date Formats and various methods to use them. Including stringToDateToStringMultiParsers(String dateString) that will try every Date Format to parse
 * the given String and fail after all tried. The stringToDateToString dance is done here to validate the String is a date and also to lose ms precision.
 * 
 */
public class GMLDateUtils {
    private static Logger logger = LoggerFactory.getLogger(GMLDateUtils.class);

    private static final ZoneId UTC = ZoneId.of("UTC");
    
    /*
     * Date formats have observed in input data:
     * 1992-08-12
     * 2011-20-07 (wrong way around)
     * 1994-05-15T00:00Z
     * 
     */
    /**
     * "uuuu-MM-dd'T'HH:mm:ssX"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_TIME_SEC =
            DateTimeFormatDecorator.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");

    /**
     * "uuuu-MM-dd'T'HH:mm:ss.SSSX"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_TIME_MILLISEC =
            DateTimeFormatDecorator.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX");

    /**
     * "uuuu-MM-dd'T'HH:mmX"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_TIME =
            DateTimeFormatDecorator.ofPattern("uuuu-MM-dd'T'HH:mmX");

    /**
     * "dd MMM uuuu HH:mm z"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_TIME_OUTPUT =
            DateTimeFormatDecorator.ofPattern("dd MMM uuuu HH:mm z");

    /**
     * "uuuu-MM-ddZ"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT =
            DateTimeFormatDecorator.ofPattern("uuuu-MM-ddX");

    /**
     * "uuuu-MM-dd"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_NO_ZONE =
            DateTimeFormatDecorator.ofPattern("uuuu-MM-dd");

    /**
     * "uuuu MMM dd"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_MONTH =
            DateTimeFormatDecorator.ofPattern("uuuu MMM dd");

    /**
     * "dd MMM uuuu"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_MONTH_BWARDS =
            DateTimeFormatDecorator.ofPattern("dd MMM uuuu");

    /**
     * "uuuu-dd-MM" - we should reject this
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_DAY_US_STYLE =
            DateTimeFormatDecorator.ofPattern("uuuu-dd-MM").withResolverStyle(ResolverStyle.SMART);

    /**
     * "dd MMM uuuu"
     */
    public static final DateTimeFormatter GEODESYML_DATE_FORMAT_OUTPUT =
            DateTimeFormatDecorator.ofPattern("dd MMM uuuu");

    /*
     * Array of 2 place arrays of formats for parsing and output (used by stringToDateToStringMultiParsers(String dateString)):
     * 
     * - first=parse, second=output or correction
     */
    private static final DateTimeFormatter[] dateFormats = new DateTimeFormatter[] {
            GEODESYML_DATE_FORMAT_TIME_MILLISEC,
            GEODESYML_DATE_FORMAT_TIME_SEC,
            GEODESYML_DATE_FORMAT_TIME,
            GEODESYML_DATE_FORMAT,
            GEODESYML_DATE_FORMAT_NO_ZONE,
            GEODESYML_DATE_FORMAT_MONTH,
            GEODESYML_DATE_FORMAT_MONTH_BWARDS
    };

    /**
     * Uses default dateFormat of GEODESYML_DATE_FORMAT_FULL
     * 
     * @param date
     */
    public static String dateToString(Instant date) {
        return dateToString(date, GEODESYML_DATE_FORMAT_TIME_SEC);
    }

    /**
     * Formats a date using the specified pattern
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Instant date, String pattern) {
        return dateToString(date, DateTimeFormatter.ofPattern(pattern).withZone(UTC));
    }
    
    /**
     * 
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToString(Instant date, DateTimeFormatter dateFormat) {
        if (date == null) {
            return null;
        }
        else {
            return dateFormat.format(date);
        }
    }

    /**
     * Uses default dateFormat of GEODESYML_DATE_FORMAT_FULL
     * 
     * @param dateString
     * @return
     * @throws DateTimeParseException
     */
    public static Instant stringToDate(String dateString) throws DateTimeParseException {
        return stringToDate(dateString, GEODESYML_DATE_FORMAT_TIME_SEC);
    }

    /**
     * Utility method to parse a String using the specified pattern
     *
     * @param dateString
     * @param pattern
     * @return
     * @throws DateTimeParseException
     */
    public static Instant stringToDate(String dateString, String pattern) throws DateTimeParseException {
        return stringToDate(dateString, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 
     * @param dateString
     * @param dateFormat
     * @return an
     * @throws DateTimeParseException
     */
    public static Instant stringToDate(String dateString, DateTimeFormatter dateFormat) throws DateTimeParseException {
        Instant parsedLocalDate = null;
        if (dateString != null) {
            try {
                parsedLocalDate = OffsetDateTime.parse(dateString, dateFormat).toInstant();
            } catch (DateTimeParseException e) {
                logger.debug(String.format("Unable to parse date string: %s, with dateFormat: %s",
                        dateString,
                        DateTimeFormatDecorator.getPattern(dateFormat)));
                try {
                    // try parsing the string into a date only (no time fields)
                    LocalDate localDate = LocalDate.parse(dateString, dateFormat);
                    parsedLocalDate = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                    return parsedLocalDate;
                } catch (DateTimeParseException e2) {
                    logger.debug(String.format("Unable to parse date string: %s, with dateFormat: %s",
                            dateString,
                            DateTimeFormatDecorator.getPattern(dateFormat)));
                }
                throw e;
            }
        }
        return parsedLocalDate;
    }

    /**
     * Perform this dateToString and stringToDate dance so can lose the ms precision that renders dates different. Uses default dateFormat of
     * GEODESYML_DATE_FORMAT_FULL
     * 
     * @param dateString
     */
    public static String stringToDateToString(String dateString) throws DateTimeParseException {
        return dateToString(stringToDate(dateString));
    }

    /**
     * Perform this dateToString and stringToDate dance so can lose the ms precision that renders dates different.
     * 
     * @param dateString
     * @param dateFormat
     *            - used for parsing and for output
     */
    public static String stringToDateToString(String dateString, DateTimeFormatter dateFormat) throws DateTimeParseException {
        return dateToString(stringToDate(dateString, dateFormat), dateFormat);
    }

    /**
     * Perform this dateToString and stringToDate dance so can lose the ms precision that renders dates different.
     * 
     * @param dateString
     *            - to parse as a date
     * @param dateFormatForParsing
     *            - used to parse input
     * @param dateFormatForOutput
     *            - used to output the date that is parsed
     */
    public static String stringToDateToString(String dateString, DateTimeFormatter dateFormatForParsing,
            DateTimeFormatter dateFormatForOutput) throws DateTimeParseException {
        return dateToString(stringToDate(dateString, dateFormatForParsing), dateFormatForOutput);
    }

    /**
     * Attempt to parse using all DateFormats, only throwing an exception to the client if all DateFormats are all exhausted. Do so by returning a string from
     * an input string going via an intermediate Date object. This technique good to validate input is a valid date. It also drops millisecond precision and
     * through the dateFormats[] array, allows a specific output format to be used.
     * 
     * @param dateString
     * @return string version of date after verify it a date
     */
    public static String stringToDateToStringMultiParsers(String dateString) {
        String result = null;
        StringBuilder formatsFailed = new StringBuilder();
        logger.debug("stringToDateToStringMultiParsers - input: " + dateString);
        for (DateTimeFormatter parseFormat : dateFormats) {
            try {
                logger.debug("  Attempt to parse with: " + DateTimeFormatDecorator.getPattern(parseFormat));
                result = stringToDateToString(dateString, parseFormat, GEODESYML_DATE_FORMAT_TIME_MILLISEC);
                break;
            } catch (DateTimeParseException e) {
                formatsFailed.append(DateTimeFormatDecorator.getPattern(parseFormat)).append(", ");
            }
        }
        if (result == null) {
            // If the dateString is "(CCYY-MM-DDThh" or "CCYY-MM-DDThh)" or "CCYY-MM-DDThh" it is most likely the template string that was somehow munged.  
            // And it was always meant to remain as a template and hence this can be ignored.
            // Note that "(CCYY-MM-DDThh)" is never passed in for translation
            if (dateString.equals("(CCYY-MM-DDThh") || dateString.equals("CCYY-MM-DDThh")
                    || dateString.equals("CCYY-MM-DDThh)")) {
                String msg = "Cannot parse dateString: '" + dateString + "' and it is likely a badly formed template. Ignoring.";
                logger.debug(msg);
            } else {
                String msg = "Cannot parse dateString: '" + dateString + "' with any of the formats: "
                        + formatsFailed.toString();
                logger.debug(msg);
                throw new GeodesyRuntimeException(msg);
            }
        }
        return result;
    }

    /**
     * Attempt to parse using all DateFormats, only throwing an exception to the client if all DateFormats are all exhausted.
     * 
     * @param dateString
     * @return date version of stringDate
     */
    public static Instant stringToDateMultiParsers(String dateString) {
        Instant result = null;
        StringBuilder formatsFailed = new StringBuilder();
        logger.debug("stringToDateMultiParsers - input: " + dateString);
        for (DateTimeFormatter parseFormat : dateFormats) {
            try {
                logger.debug("  Attempt to parse with: " + DateTimeFormatDecorator.getPattern(parseFormat));
                result = stringToDate(dateString, parseFormat);
                break;
            } catch (DateTimeParseException e) {
                formatsFailed.append(DateTimeFormatDecorator.getPattern(parseFormat)).append(", ");
            }
        }
        if (result == null) {
            String msg = "Cannot parse dateString: " + dateString + " with any of the formats: "
                    + formatsFailed.toString();
            logger.debug(msg);
            throw new GeodesyRuntimeException(msg);
        }
        return result;
    }
}
