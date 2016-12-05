package au.gov.ga.geodesy.support.mapper.orika;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;
import net.opengis.iso19139.gco.v_20070417.ObjectFactory;

public class StringToStringPropertyConverter extends BidirectionalConverter<String, CharacterStringPropertyType> {

    public CharacterStringPropertyType convertTo(String s, Type<CharacterStringPropertyType> destType, MappingContext ctx) {
        CharacterStringPropertyType property = new CharacterStringPropertyType();
        property.setCharacterString(new ObjectFactory().createCharacterString(s));
        return property;
    }

    public String convertFrom(CharacterStringPropertyType stringProperty, Type<String> destType, MappingContext ctx) {
        if (stringProperty == null || stringProperty.getCharacterString() == null) {
            return null;
        }
        return (String) stringProperty.getCharacterString().getValue();
    }
}

