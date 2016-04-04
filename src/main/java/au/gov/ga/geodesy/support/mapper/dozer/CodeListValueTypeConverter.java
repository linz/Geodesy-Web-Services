package au.gov.ga.geodesy.support.mapper.dozer;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opengis.iso19139.gco.v_20070417.CodeListValueType;

/**
 * Convert: java.lang.String <--> net.opengis.gml.v_3_2_1.CodeListValueType
 * 
 * Also handles these extending classes:
 * au.gov.xml.icsm.geodesyml.v_0_3.IgsReceiverModelCodeType
 * au.gov.xml.icsm.geodesyml.v_0_3.IgsAntennaModelCodeType
 * 
 */
public class CodeListValueTypeConverter implements CustomConverter {
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
            CodeListValueType destClassInstanceType =  (CodeListValueType) destClassInstance;
            destClassInstanceType.setValue((String) source);
            destClassInstanceType.setCodeList("codelist");
            destClassInstanceType.setCodeListValue("codeListValue");
            return destClassInstance;
        } else if (source instanceof CodeListValueType) {
            return ((CodeListValueType) source).getValue();
        } else {
            throw new MappingException("Converter " + getClass().getName()
                    + "used incorrectly. Arguments passed in were:" + destination + " and " + source);
        }
    }
}
