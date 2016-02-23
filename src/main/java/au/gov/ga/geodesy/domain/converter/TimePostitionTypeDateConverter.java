package au.gov.ga.geodesy.domain.converter;

import java.util.Date;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * Converter: java.util.Date <--> net.opengis.gml.v_3_2_1.TimePositionType
 */
public class TimePostitionTypeDateConverter implements CustomConverter {

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof Date) {
            TimePositionType dest = null;
            if (destination == null) {
                dest = new TimePositionType();
            } else {
                dest = (TimePositionType) destination;
            }
            dest.getValue().add(GMLDateUtils.dateToString((Date) source, GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_OUTPUT));
            return dest;
        } else if (source instanceof TimePositionType) {
            Date dest = GMLDateUtils.stringToDateMultiParsers(((TimePositionType) source).getValue().get(0));
            return dest;
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }
}
