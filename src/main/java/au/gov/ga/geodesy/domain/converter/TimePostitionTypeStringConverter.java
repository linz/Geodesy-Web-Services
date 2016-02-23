package au.gov.ga.geodesy.domain.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * Converter: java.lang.String <--> net.opengis.gml.v_3_2_1.TimePositionType
 */
public class TimePostitionTypeStringConverter implements CustomConverter {

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof String) {
            TimePositionType dest = null;
            if (destination == null) {
                dest = new TimePositionType();
            } else {
                dest = (TimePositionType) destination;
            }
            dest.getValue().add(dateParse((String) source));
            return dest;
        } else if (source instanceof TimePositionType) {
            String dest = GMLDateUtils.stringToDateToStringMultiParsers(((TimePositionType) source).getValue().get(0));
            return dest;
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }

    private String dateParse(String dateString) {
        try {
            return GMLDateUtils.stringToDateToStringMultiParsers(dateString);
        } catch (GeodesyRuntimeException e) {
            throw new MappingException("Converter TestCustomConverter " + " - date parse problem for: " + dateString);
        }
    }
}
