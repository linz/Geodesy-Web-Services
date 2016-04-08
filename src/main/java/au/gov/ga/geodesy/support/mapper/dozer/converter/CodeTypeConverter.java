package au.gov.ga.geodesy.support.mapper.dozer.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opengis.gml.v_3_2_1.CodeType;

/**
 * Convert: java.lang.String <--> net.opengis.gml.v_3_2_1.CodeType
 * 
 * Also for these extending classes:
 * net.opengis.gml.v_3_2_1.CodeWithAuthorityType
 * au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType
 */
public class CodeTypeConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }

        if (source instanceof String) {
            Object destClassInstance = null;
            if (destination == null) {
                try {
                    destClassInstance = destClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("Error createing Dozer destination", e);
                }
            } else {
                destClassInstance = destination;
            }
            ((CodeType) destClassInstance).setValue((String) source);
            return destClassInstance;
        } else if (source instanceof CodeType) {
            return ((CodeType) source).getValue();
        } else {
            throw new MappingException("Converter " + getClass().getName()
                    + "used incorrectly. Arguments passed in were:" + destination + " and " + source);
        }
    }
}
