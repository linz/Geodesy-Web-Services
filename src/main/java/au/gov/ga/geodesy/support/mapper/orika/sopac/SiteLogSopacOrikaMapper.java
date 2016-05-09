package au.gov.ga.geodesy.support.mapper.orika.sopac;

import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.opengis.metadata.citation.Telephone;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.Agency;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.port.adapter.sopac.SiteLogSopacMapper;
import au.gov.ga.geodesy.support.mapper.orika.StringToInternationalStringConverter;
import au.gov.ga.geodesy.support.mapper.orika.StringToStringPropertyConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.OrikaSystemProperties;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;

@Component
public class SiteLogSopacOrikaMapper implements SiteLogSopacMapper {

    static {
        System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES, "true");
    }

    private MapperFacade mapper;

    public SiteLogSopacOrikaMapper() {
        MapperFactory factory = new DefaultMapperFactory.Builder()
            .compilerStrategy(new EclipseJdtCompilerStrategy())
            .build();

        factory.classMap(IgsSiteLog.class, SiteLog.class)
            .field("contactAgency", "siteContact.party")
            .field("responsibleAgency", "siteMetadataCustodian.party")
            .exclude("equipmentLogItems")
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
        mapper = factory.getMapperFacade();
    }

    public SiteLog fromDTO(IgsSiteLog siteLogSopac) {
        return mapper.map(siteLogSopac, SiteLog.class);
    }

    public IgsSiteLog toDTO(SiteLog siteLog) {
        return mapper.map(siteLog, IgsSiteLog.class);
    }
}
