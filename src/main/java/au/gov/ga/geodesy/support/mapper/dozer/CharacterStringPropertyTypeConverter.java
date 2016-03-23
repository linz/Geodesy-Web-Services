package au.gov.ga.geodesy.support.mapper.dozer;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;
import net.opengis.iso19139.gco.v_20070417.ObjectFactory;


/**
 * Convert: net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType <-->
 *          java.lang.String
 */
public class CharacterStringPropertyTypeConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());
    ObjectFactory gcoObjectFactory = new ObjectFactory();

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }

        if (source instanceof String) {
            CharacterStringPropertyType dest = null;
            if (destination == null) {
                dest = new CharacterStringPropertyType();
            } else {
                dest = (CharacterStringPropertyType) destination;
            }
            dest.setCharacterString(gcoObjectFactory.createCharacterString((String) source));
            return dest;
        } else if (source instanceof CharacterStringPropertyType) {
            return ((CharacterStringPropertyType) source).getCharacterString().getValue();
        } else {
            throw new MappingException("Converter " + getClass().getName()
                    + "used incorrectly. Arguments passed in were:" + destination + " and " + source);
        }
    }
}
