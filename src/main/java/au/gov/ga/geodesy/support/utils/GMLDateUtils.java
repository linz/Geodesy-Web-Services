package au.gov.ga.geodesy.support.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

    /*
     * Date formats have observed in input data:
     * 1992-08-12
     * 2011-20-07 (wrong way around)
     * 1994-05-15T00:00Z
     * 
     */
    /**
     * "yyyy-MM-dd'T'HH:mm:ssX"
     */
    public static final DateFormat GEODESYML_DATE_FORMAT_TIME_SEC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    /**
     * "yyyy-MM-dd'T'HH:mmX"
     */
    public static final DateFormat GEODESYML_DATE_FORMAT_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmX");
    /**
     * "dd MMM yyyy HH:mm z"
     */
    public static final DateFormat GEODESYML_DATE_FORMAT_TIME_OUTPUT = new SimpleDateFormat("dd MMM yyyy HH:mm z");
    /**
     * "yyyy-MM-dd"
     */
    public static final DateFormat GEODESYML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * "yyyy MMM dd"
     */
    public static final DateFormat GEODESYML_DATE_FORMAT_MONTH = new SimpleDateFormat("yyyy MMM dd");
    /**
     * "dd MMM yyyy"
     */
    public static final DateFormat GEODESYML_DATE_FORMAT_MONTH_BWARDS = new SimpleDateFormat("dd MMM yyyy");
    /**
     * "yyyy-dd-MM"
     */
    private static final DateFormat GEODESYML_DATE_FORMAT_BAD = new SimpleDateFormat("yyyy-dd-MM");
    /**
     * "dd MMM yyyy"
     */
    public static final DateFormat GEODESYML_DATE_FORMAT_OUTPUT = new SimpleDateFormat("dd MMM yyyy");

    /*
     * Array of 2 place arrays of formats for parsing and output (used by stringToDateToStringMultiParsers(String dateString)):
     * 
     * - first=parse, second=output or correction
     */
    private static final DateFormat[][] dateFormats = new DateFormat[][] {
            new DateFormat[] {GEODESYML_DATE_FORMAT_TIME_SEC, GEODESYML_DATE_FORMAT_TIME_OUTPUT},
            new DateFormat[] {GEODESYML_DATE_FORMAT_TIME, GEODESYML_DATE_FORMAT_TIME_OUTPUT},
            new DateFormat[] {GEODESYML_DATE_FORMAT, GEODESYML_DATE_FORMAT_OUTPUT},
            new DateFormat[] {GEODESYML_DATE_FORMAT_BAD, GEODESYML_DATE_FORMAT_OUTPUT},
            new DateFormat[] {GEODESYML_DATE_FORMAT_MONTH, GEODESYML_DATE_FORMAT_MONTH},
            new DateFormat[] {GEODESYML_DATE_FORMAT_MONTH_BWARDS, GEODESYML_DATE_FORMAT_MONTH_BWARDS}};

    static {
        GEODESYML_DATE_FORMAT_TIME_SEC.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT_TIME_SEC.setLenient(false);
        GEODESYML_DATE_FORMAT_TIME.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT_TIME.setLenient(false);
        GEODESYML_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT.setLenient(false);
        GEODESYML_DATE_FORMAT_BAD.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT_BAD.setLenient(false);
        GEODESYML_DATE_FORMAT_TIME_OUTPUT.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT_TIME_OUTPUT.setLenient(false);
        GEODESYML_DATE_FORMAT_MONTH.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT_MONTH.setLenient(false);
        GEODESYML_DATE_FORMAT_MONTH_BWARDS.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT_MONTH_BWARDS.setLenient(false);
        GEODESYML_DATE_FORMAT_OUTPUT.setTimeZone(TimeZone.getTimeZone("GMT"));
        GEODESYML_DATE_FORMAT_OUTPUT.setLenient(false);
    }

    /**
     * Uses default dateFormat of GEODESYML_DATE_FORMAT_FULL
     * 
     * @param date
     */
    public static String dateToString(Date date) {
        return dateToString(date, GEODESYML_DATE_FORMAT_TIME_SEC);
    }

    /**
     * 
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToString(Date date, DateFormat dateFormat) {
        if (date == null)
            return null;
        else
            return dateFormat.format(date);
    }

    /**
     * Uses default dateFormat of GEODESYML_DATE_FORMAT_FULL
     * 
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateString) throws ParseException {
        return stringToDate(dateString, GEODESYML_DATE_FORMAT_TIME_SEC);
    }

    /**
     * 
     * @param dateString
     * @param dateFormat
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateString, DateFormat dateFormat) throws ParseException {
        Date date = null;
        if (dateString == null)
            return null;
        else
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                logger.debug(String.format("Unable to parse date string: %s, with dateFormat: %s", dateString,
                        ((SimpleDateFormat) dateFormat).toLocalizedPattern()));
                throw e;
            }
        return date;
    }

    /**
     * Perform this dateToString and stringToDate dance so can lose the ms precision that renders dates different. Uses default dateFormat of
     * GEODESYML_DATE_FORMAT_FULL
     * 
     * @param date
     */
    public static String stringToDateToString(String dateString) throws ParseException {
        return dateToString(stringToDate(dateString));
    }

    /**
     * Perform this dateToString and stringToDate dance so can lose the ms precision that renders dates different.
     * 
     * @param date
     * @param dateFormat
     *            - used for parsing and for output
     */
    public static String stringToDateToString(String dateString, DateFormat dateFormat) throws ParseException {
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
    public static String stringToDateToString(String dateString, DateFormat dateFormatForParsing,
            DateFormat dateFormatForOutput) throws ParseException {
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
        for (DateFormat[] dfPairs : dateFormats) {
            try {
                logger.debug("  Attempt to parse with: " + ((SimpleDateFormat) dfPairs[0]).toPattern());
                result = stringToDateToString(dateString, dfPairs[0], dfPairs[1]);
                break;
            } catch (ParseException e) {
                formatsFailed.append(((SimpleDateFormat) dfPairs[0]).toPattern()).append(", ");
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
    public static Date stringToDateMultiParsers(String dateString) {
        Date result = null;
        StringBuilder formatsFailed = new StringBuilder();
        logger.debug("stringToDateMultiParsers - input: " + dateString);
        for (DateFormat[] dfPairs : dateFormats) {
            try {
                logger.debug("  Attempt to parse with: " + ((SimpleDateFormat) dfPairs[0]).toPattern());
                result = stringToDate(dateString, dfPairs[0]);
                break;
            } catch (ParseException e) {
                formatsFailed.append(((SimpleDateFormat) dfPairs[0]).toPattern()).append(", ");
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

    public static Date buildStartOfTime() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(ZoneId.of("+11")));
        cal.set(Calendar.YEAR,1970);
        cal.set(Calendar.MONTH,Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH,23);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return cal.getTime();
    }
}
