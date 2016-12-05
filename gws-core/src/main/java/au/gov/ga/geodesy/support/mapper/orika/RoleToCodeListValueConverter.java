package au.gov.ga.geodesy.support.mapper.orika;

import java.util.HashMap;
import java.util.Map;

import org.opengis.metadata.citation.Role;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import net.opengis.iso19139.gco.v_20070417.CodeListValueType;

public class RoleToCodeListValueConverter extends BidirectionalConverter<Role, CodeListValueType> {

    // TODO: complete, use google bimaps or derive programmatically
    static Map<Role, String> values = new HashMap<>();
    static {
        values.put(Role.POINT_OF_CONTACT, "pointOfContact");
    }

    static Map<String, Role> roles = new HashMap<>();
    static {
        roles.put("pointOfContact", Role.POINT_OF_CONTACT);
    }

    public CodeListValueType convertTo(Role role, Type<CodeListValueType> destType,
            MappingContext ctx) {

        String value = values.get(role);
        CodeListValueType codeListValue = new CodeListValueType();
        codeListValue.setValue(value);
        codeListValue.setCodeList("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode");
        codeListValue.setCodeListValue(value);
        codeListValue.setCodeSpace("ISOTC211/19115");
        return codeListValue;
    }

    public Role convertFrom(CodeListValueType codeListValue, Type<Role> destType,
            MappingContext ctx) {
        return roles.get(codeListValue.getValue());
    }
}
