package au.gov.ga.geodesy.support.mapper.orika;

import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.geotools.util.SimpleInternationalString;
import org.opengis.metadata.citation.Address;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.util.InternationalString;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import net.opengis.iso19139.gmd.v_20070417.CIAddressType;
import net.opengis.iso19139.gmd.v_20070417.CIContactType;
import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;
import net.opengis.iso19139.gmd.v_20070417.CITelephoneType;

public class ResponsiblePartyOrikaMapper {

    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    MapperFacade mapper;

    public ResponsiblePartyOrikaMapper() {

        mapperFactory.classMap(Address.class, CIAddressType.class)
            .field("deliveryPoints", "deliveryPoint")
            .field("electronicMailAddresses", "electronicMailAddress")
            .byDefault()
            .register();

        mapperFactory.classMap(ResponsiblePartyImpl.class, CIResponsiblePartyType.class)
            .field("role", "role.CIRoleCode")
            .field("contactInfo", "contactInfo.CIContact")
            .byDefault()
            .register();

        mapperFactory.classMap(ContactImpl.class, CIContactType.class)
            .field("address", "address.CIAddress")
            .field("phone", "phone.CITelephone")
            .byDefault()
            .register();

        mapperFactory.classMap(TelephoneImpl.class, CITelephoneType.class)
            .field("voices", "voice")
            .field("facsimiles", "facsimile")
            .byDefault()
            .register();

        mapperFactory.getConverterFactory().registerConverter(new InternationalStringToStringPropertyConverter());
        mapperFactory.getConverterFactory().registerConverter(new RoleToCodeListValueConverter());
        mapperFactory.getConverterFactory().registerConverter(new StringToStringPropertyConverter());
        mapperFactory.registerConcreteType(InternationalString.class, SimpleInternationalString.class);
        mapper = mapperFactory.getMapperFacade();
    }

    public ResponsibleParty mapFromDto(CIResponsiblePartyType partyType) {
        return mapper.map(partyType, ResponsibleParty.class);
    }

    public CIResponsiblePartyType mapToDto(ResponsibleParty party) {
        return mapper.map(party, CIResponsiblePartyType.class);
    }
}
