package au.gov.ga.geodesy.support.mapper.orika.sopac;

import java.time.Instant;

import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.citation.Telephone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.ContactType;
import au.gov.ga.geodesy.domain.model.ContactTypeRepository;
import au.gov.ga.geodesy.domain.model.SiteResponsibleParty;
import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.Agency;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogMapper;
import au.gov.ga.geodesy.support.mapper.orika.StringToInternationalStringConverter;
import au.gov.ga.geodesy.support.mapper.orika.StringToStringPropertyConverter;
import au.gov.ga.geodesy.support.mapper.orika.geodesyml.InstantToTimePositionConverter;
import au.gov.ga.geodesy.support.mapper.orika.geodesyml.JAXBElementConverter;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import net.opengis.gml.v_3_2_1.TimePeriodType;

@Component
public class SiteLogSopacOrikaMapper implements SopacSiteLogMapper {
    private MapperFacade mapper;

    @Autowired
    private ContactTypeRepository contactTypes;

    public SiteLogSopacOrikaMapper() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(IgsSiteLog.class, SiteLog.class)
                .field("entryDate", "entryDate")
                .exclude("equipmentLogItems")
                .customize(new CustomMapper<IgsSiteLog, SiteLog>() {

                    private SiteResponsibleParty fromDto(Agency agency, ContactType contactType, MappingContext ctx) {
                        ResponsibleParty party = this.mapperFacade.map(agency, ResponsiblePartyImpl.class);
                        return new SiteResponsibleParty(contactType.getId(), party);
                    }

                    @Override
                    public void mapAtoB(IgsSiteLog igsSiteLog, SiteLog siteLog, MappingContext ctx) {
                        siteLog.getResponsibleParties().add(
                            fromDto(igsSiteLog.getContactAgency(), contactTypes.siteContact(), ctx));

                        siteLog.getResponsibleParties().add(
                            fromDto(igsSiteLog.getContactAgency(), contactTypes.siteMetadataCustodian(), ctx));
                    }
                })
                .byDefault()
                .register();

        factory.classMap(Agency.class, ResponsiblePartyImpl.class)
                .field("name", "organisationName")
                .field("primaryContact.name", "individualName")
                .field("primaryContact", "contactInfo")
                .byDefault()
                .register();

        factory.classMap(au.gov.ga.geodesy.igssitelog.domain.model.Contact.class, ContactImpl.class)
                .fieldMap("telephonePrimary", "phone.voices.fst:{|add(%s)}").mapNulls(false).add()
                .fieldMap("telephoneSecondary", "phone.voices.fst:{|add(%s)}").mapNulls(false).add()
                .fieldMap("fax", "phone.facsimiles.fst:{|add(%s)}").mapNulls(false).add()
                .byDefault()
                .register();

        factory.getConverterFactory().registerConverter(new StringToInternationalStringConverter());
        factory.getConverterFactory().registerConverter(new StringToStringPropertyConverter());
        factory.registerConcreteType(Telephone.class, TelephoneImpl.class);
        factory.getConverterFactory().registerConverter(new PassThroughConverter(Instant.class));
        factory.getConverterFactory().registerConverter("jaxbElementConverter", new JAXBElementConverter<TimePeriodType, EffectiveDates>() {});
        factory.getConverterFactory().registerConverter(new InstantToTimePositionConverter());
        mapper = factory.getMapperFacade();
    }

    public SiteLog fromDTO(IgsSiteLog siteLogSopac) {
        return mapper.map(siteLogSopac, SiteLog.class);
    }

    public IgsSiteLog toDTO(SiteLog siteLog) {
        return mapper.map(siteLog, IgsSiteLog.class);
    }
}
