package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.xml.icsm.geodesyml.v_0_4.CountryCodeType;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class StringToCountryCodeTypeConverter extends BidirectionalConverter<String, CountryCodeType> {

    private String codeSpace;

    public StringToCountryCodeTypeConverter(String codeSpace) {
        this.codeSpace = codeSpace;
    }

    @Override
    public String convertFrom(CountryCodeType code, Type<String> targetType, MappingContext ctx) {
        return code.getValue();
    }

    @Override
    public CountryCodeType convertTo(String str, Type<CountryCodeType> targetType, MappingContext ctx) {
        CountryCodeType code = new CountryCodeType();
        code.setValue(str);
        code.setCodeSpace(codeSpace);
        return code;
    }
}
