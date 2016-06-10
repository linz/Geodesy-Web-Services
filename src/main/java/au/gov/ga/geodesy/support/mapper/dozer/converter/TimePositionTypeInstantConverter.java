package au.gov.ga.geodesy.support.mapper.dozer.converter;

import java.time.Instant;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * Converter: java.time.Instant <--> net.opengis.gml.v_3_2_1.TimePositionType
 */
public class TimePositionTypeInstantConverter implements CustomConverter {

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof Instant) {
            TimePositionType dest = null;
            if (destination == null) {
                dest = new TimePositionType();
            } else {
                dest = (TimePositionType) destination;
            }
            Instant sourceType = (Instant) source;
            String destDate = GMLDateUtils.dateToString(sourceType, GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_SEC);
            dest.getValue().add(destDate);
            return dest;
        } else if (source instanceof TimePositionType) {
            Instant dest = GMLDateUtils.stringToDateMultiParsers(((TimePositionType) source).getValue().get(0));
            return dest;
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }
}
