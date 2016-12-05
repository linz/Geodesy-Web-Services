package au.gov.ga.geodesy.support.mapper.orika;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class StringToInternationalStringConverter extends BidirectionalConverter<String, InternationalString> {

    public InternationalString convertTo(String s, Type<InternationalString> destType,
            MappingContext ctx) {

        return new SimpleInternationalString(s);
    }

    public String convertFrom(InternationalString s, Type<String> destType, MappingContext ctx) {
        return s.toString();
    }
}
