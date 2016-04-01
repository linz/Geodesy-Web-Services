package au.gov.ga.geodesy.support.mapper.dozer;

import java.math.BigDecimal;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;

/**
 * Convert: java.lang.String <--> java.math.BigDecimal
 * 
 * A recent change was to make the type of an element Doouble instead of String. JaxB's xjc is turning some of those into BigDecimals
 * instead. Manage possible problem values.
 * 
 */
public class StringBigDecimalConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof String) {
            // use the String <--> Double converter
            Double doubleVal = DozerDelegate.mapWithGuard((String) source, Double.class);

            BigDecimal dest = BigDecimal.valueOf(doubleVal);
            return dest;
        } else if (source instanceof BigDecimal) {
            BigDecimal sourceType = (BigDecimal) source;
            return new Double(sourceType.doubleValue()).toString();
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }
}
