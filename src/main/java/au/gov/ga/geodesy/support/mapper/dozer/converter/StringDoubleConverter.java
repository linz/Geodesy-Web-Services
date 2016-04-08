package au.gov.ga.geodesy.support.mapper.dozer.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert: java.lang.String <--> java.lang.Double
 * 
 * A recent change was to make the type of an element Double instead of String. Manage possible problem values.
 * 
 */
public class StringDoubleConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof String) {
            Double dest = null;
            try {
                dest = Double.parseDouble((String) source);
            } catch (NumberFormatException e) {
                dest = Double.parseDouble(handleProblemValues((String) source));
            }
            return dest;
        } else if (source instanceof Double) {
            Double sourceType = (Double) source;
            return sourceType.toString();
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }

    /**
     * Deal with possible problem Strings, returning a Double equivalent or 0.0.
     * 
     * @param source
     *            to find alt Double-like value
     * @return Double equivalent of given String or 0.0
     */
    private String handleProblemValues(String source) {
        switch (source.toUpperCase()) {
        case "NONE":
            return "0.0";
        }
        // drop "+-" or "-+" with optional '/' between them
        String dropPlusMinux = source.replaceAll("\\+\\/?-|-\\/?\\+", "");
        
        // drop "APPROX." (optional .)
        String dropApprox = dropPlusMinux.replaceAll("APPROX.?", "");

        // Now just drop anything but numbers, + -, .
        String dropRest = dropApprox.replaceAll("[^\\d\\.\\+-]+", "");// .replaceAll("[^\\.]+", "");//\\.+-]+", "");

        // Special cases
        // "a...b" -> "a"
        String sc1 = dropRest.replaceAll("\\.\\.+.*", "");

        // Special cases
        // "a.b.c" -> "a.b"
        String sc2 = sc1.replaceAll("(\\d+\\.\\d+)\\.\\d+.*", "$1");

        // Special cases
        // "a+b" -> "a" (or "a-b" -> "a")
        String sc3 = sc2.replaceAll("(\\d+)[\\+\\-]\\d+.*", "$1");

        // But in some instances there are no numbers at all so just make the result 0.0 and log the ERROR
        String removeDigits = sc3.replaceAll("[\\d]", "");
        if (sc3.length() == removeDigits.length()) {
            logger.error("Source doesn't contain any numbers - fix data: \"" + source + "\"");
            return "0.0";
        }

        if (sc3.length() < dropRest.length()) {
            logger.error(String.format(
                    "Source data that represents a double is likely in bad format - source: %s, returning: %s ", source,
                    sc2));
        }

        return sc3;
    }

}
