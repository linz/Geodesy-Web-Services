package au.gov.ga.geodesy.support.mapper.dozer.converter;

import org.dozer.CustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType;

/**
 * Convert field: au.gov.ga.geodesy.igssitelog.domain.model.GnssAntennaLogItem.antennaRadomeType
 * <--> au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaType.antennaRadomeType
 * 
 * Specifically need to add missing attributes.
 * 
 */
public class AntennaRadomeTypeConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());
    public static final String DEFAULT_CODESPACEATTR = "CodeSpaceAttr";

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }

        // just field converter so don't need all that fancy wiring and type checking

        String destValue = (String) source;
        String codeSpaceAttr = DEFAULT_CODESPACEATTR; // dummy for now
        IgsRadomeModelCodeType dest = null;
        if (destination == null) {
            dest = new IgsRadomeModelCodeType();
        } else {
            dest = (IgsRadomeModelCodeType) destination;
        }

        dest.setValue(destValue);
        dest.setCodeSpace(codeSpaceAttr);
        return dest;
    }
}
