package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

public class StringToListConverter<T> extends BidirectionalConverter<String, List<T>> {

    private BidirectionalConverter<String, T> elementConverter;
    private Type<T> elementType;

    public StringToListConverter(BidirectionalConverter<String, T> elementConverter, Type<T> elementType) {
        this.elementConverter = elementConverter;
    }

    public String convertFrom(List<T> codes, Type<String> targetType, MappingContext ctx) {
        return codes.stream()
            .map(code -> elementConverter.convertFrom(code, TypeFactory.valueOf(String.class), ctx))
            .reduce(new BinaryOperator<String>() {
                public String apply(String a, String b) {
                    return a + "," + b;
                }
            })
            .orElse(null); 
    }

    public List<T> convertTo(String str, Type<List<T>> targetType, MappingContext ctx) {
        return Arrays.stream(str.split(","))
            .map(s -> elementConverter.convertTo(s, elementType, ctx))
            .collect(Collectors.toList());
    }
}
