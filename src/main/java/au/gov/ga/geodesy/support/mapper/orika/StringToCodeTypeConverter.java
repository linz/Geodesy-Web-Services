package au.gov.ga.geodesy.support.mapper.orika;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import net.opengis.gml.v_3_2_1.CodeType;

public class StringToCodeTypeConverter extends BidirectionalConverter<String, CodeType> {

    private String codeSpace;

    public StringToCodeTypeConverter(String codeSpace) {
        this.codeSpace = codeSpace;
    }

    public String convertFrom(CodeType code, Type<String> targetType, MappingContext ctx) {
        return code.getValue();
    }

    public CodeType convertTo(String str, Type<CodeType> targetType, MappingContext ctx) {
        CodeType code = new CodeType();
        code.setValue(str);
        code.setCodeSpace(codeSpace);
        return code;
    }
}
