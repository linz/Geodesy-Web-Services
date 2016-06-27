package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import net.opengis.gml.v_3_2_1.CodeWithAuthorityType;

public class StringToCodeWithAuthorityTypeConverter<T extends CodeWithAuthorityType> extends BidirectionalConverter<String, T> {
    private String codeSpace;

    public StringToCodeWithAuthorityTypeConverter(String codeSpace) {
        this.codeSpace = codeSpace;
    }

    @Override
    public String convertFrom(T code, Type<String> targetType, MappingContext ctx) {
        return code.getValue();
    }

    @Override
    public T convertTo(String str, Type<T> targetType, MappingContext ctx) {
        T code = null;
        try {
            code = targetType.getRawType().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new GeodesyRuntimeException("Error with convert: ", e);
        }
        code.setValue(str);
        code.setCodeSpace(codeSpace);
        return code;
    }
}
