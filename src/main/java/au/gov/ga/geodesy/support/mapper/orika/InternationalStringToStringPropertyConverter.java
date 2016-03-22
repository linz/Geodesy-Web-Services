package au.gov.ga.geodesy.support.mapper.orika;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;
import net.opengis.iso19139.gco.v_20070417.ObjectFactory;

public class InternationalStringToStringPropertyConverter extends BidirectionalConverter<InternationalString, CharacterStringPropertyType> {

    public CharacterStringPropertyType convertTo(InternationalString s, Type<CharacterStringPropertyType> destType,
            MappingContext ctx) {

        CharacterStringPropertyType property = new CharacterStringPropertyType();
        property.setCharacterString(new ObjectFactory().createCharacterString(s.toString()));
        return property;
    }

    public InternationalString convertFrom(CharacterStringPropertyType stringP, Type<InternationalString> destType,
            MappingContext ctx) {

        return new SimpleInternationalString(stringP.toString());
    }
}
