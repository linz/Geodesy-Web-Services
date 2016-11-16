package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import net.opengis.iso19139.gco.v_20070417.CodeListValueType;

public class StringToCodeListValueConverter<T extends CodeListValueType> extends BidirectionalConverter<String, T> {

    private String codeSpace;
    private String codeList;

    public StringToCodeListValueConverter(String codeSpace, String codeList) {
        this.codeSpace = codeSpace;
        this.codeList = codeList;
    }

    @Override
    public String convertFrom(T code, Type<String> targetType, MappingContext ctx) {
        return code.getValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convertTo(String str, Type<T> targetType, MappingContext ctx) {
        try {
            return (T) (targetType.getRawType().newInstance())
                .withCodeSpace(codeSpace)
                .withCodeList(codeList)
                .withCodeListValue(str)
                .withValue(str);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new GeodesyRuntimeException("Error with convert: ", e);
        }
    }
}
