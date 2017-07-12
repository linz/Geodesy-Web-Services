package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.xml.icsm.geodesyml.v_0_4.CountryCodeType;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class StringToCountryCodeTypeConverter extends BidirectionalConverter<String, CountryCodeType> {

    private static final String codeSpace = "ISO 3166-1 alpha-3";
    private static final String codeList = "http://xml.gov.au/icsm/geodesyml/codelists/country-codes-codelist.xml#GeodesyML_CountryCode";

    @Override
    public String convertFrom(CountryCodeType code, Type<String> targetType, MappingContext ctx) {
        return code.getValue();
    }

    @Override
    public CountryCodeType convertTo(String str, Type<CountryCodeType> targetType, MappingContext ctx) {
        return (CountryCodeType) new CountryCodeType()
            .withCodeSpace(codeSpace)
            .withCodeList(codeList)
            .withCodeListValue(str)
            .withValue(str);
    }
}
