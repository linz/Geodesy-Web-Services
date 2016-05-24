package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.math.BigDecimal;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

/**
 * Orika converter for converting BigDecimal values to their equivalent String values and vice versa.
 */
public class BigDecimalToStringConverter extends BidirectionalConverter<BigDecimal, String> {

    @Override
    public String convertTo(BigDecimal source, Type<String> destinationType, MappingContext context) {
        return source.toPlainString();
    }

    @Override
    public BigDecimal convertFrom(String source, Type<BigDecimal> destinationType, MappingContext context) {
        return new BigDecimal(source);
    }
}
