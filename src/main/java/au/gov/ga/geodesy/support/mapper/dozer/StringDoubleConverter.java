package au.gov.ga.geodesy.support.mapper.dozer;

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
        // drop "+-" or "-+"
        String dropPlusMinux = source.replaceAll("\\+-|-\\+", "");
        // drop % relative humidity
        String dropRelH = dropPlusMinux.toUpperCase().replaceAll("\\% REL H", "");
        // drop any units suffix
        String dropUnits = dropRelH.toUpperCase().replaceAll("[A-Z]+\\W*[A-Z]*$", "");
        String finaldropUnits = dropUnits.length() > 0 ? dropUnits : "0.0";

        // If there are any non-digit chars left then the input was BAD. To allow translate to go through drop everything else and log an
        // ERRORÏ
        String finalResult = null;

        if (finaldropUnits.toUpperCase().matches("[\\W\\d]*[A-Z]+.*")) {
            logger.error("source cannot be made purely numeric - fix source data: " + source);
            finalResult = finaldropUnits.toUpperCase().replaceAll("[A-Z\\W]", "");
        } else {
            finalResult = finaldropUnits;
        }
        String superFinalResult = finalResult.length() > 0 ? finalResult : "0.0";

        logger.debug("handleProblemValues - input: " + source + ", output: " + superFinalResult);
        return superFinalResult;
    }

}
