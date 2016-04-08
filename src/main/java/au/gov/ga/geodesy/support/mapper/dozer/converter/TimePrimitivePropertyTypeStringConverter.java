package au.gov.ga.geodesy.support.mapper.dozer.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;

/**
 * Convert: java.lang.String <--> net.opengis.gml.v_3_2_1.TimePrimitivePropertyType
 * 
 * This use will be for net.opengis.gml.v_3_2_1.TimeInstantType (single Date) payloads.
 */
public class TimePrimitivePropertyTypeStringConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof String) {
            TimePrimitivePropertyType dest = TimePrimitivePropertyTypeUtils.addTimeInstantType(
                    TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(destination));
            TimePrimitivePropertyTypeUtils.getTheTimeInstantType(dest)
                    .setTimePosition(TimePrimitivePropertyTypeUtils.buildTimePositionType((String) source));

            return dest;
        } else if (source instanceof TimePrimitivePropertyType) {
            String dest = null;
            TimeInstantType timeInstantType = TimePrimitivePropertyTypeUtils
                    .getTheTimeInstantType((TimePrimitivePropertyType) source);
            dest = GMLDateUtils.stringToDateToStringMultiParsers((timeInstantType.getTimePosition().getValue().get(0)));
            return dest;
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }

}
