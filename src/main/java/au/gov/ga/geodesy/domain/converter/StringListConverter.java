package au.gov.ga.geodesy.domain.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import net.opengis.gml.v_3_2_1.CodeType;

/**
 * Convert: java.lang.String <--> java.util.List<CodeType>
 */
public class StringListConverter implements CustomConverter {

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        List<CodeType> dest = null;
        if (source instanceof String) {
            if (destination != null && destination instanceof List && (((List) destination).size() > 0)) {
                return (List) destination;
            }
            dest = new ArrayList<>();
            CodeType codeType = new CodeType();
            codeType.setValue((String) source);
            dest.add(codeType);
            return dest;
        } else if (source instanceof List) {
            return ((List) source).get(0);
        } else {
            throw new MappingException("Converter " + this.getClass().getName()
                    + "used incorrectly. Arguments passed in were:" + destination + " and " + source);
        }
    }
}
