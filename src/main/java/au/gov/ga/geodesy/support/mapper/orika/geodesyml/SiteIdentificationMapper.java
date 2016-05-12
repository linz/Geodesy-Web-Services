package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.util.function.Function;

import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteIdentificationType;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Reversible mapping between GeodesyML SiteIdentificationType DTO and
 * SiteIdentification site log entity.
 */
public class SiteIdentificationMapper implements Iso<SiteIdentificationType, SiteIdentification> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapper;

    public SiteIdentificationMapper() {
        mapperFactory.classMap(SiteIdentificationType.class, SiteIdentification.class)
            .field("fourCharacterID", "fourCharacterId")
            .fieldMap("monumentDescription", "monumentDescription").converter("monumentDescription").add()
            .field("heightOfTheMonument", "heightOfMonument")
            .fieldMap("geologicCharacteristic", "geologicCharacteristic").converter("geologicCharacteristic").add()
            .fieldMap("faultZonesNearby", "faultZonesNearby").converter("faultZonesNearby").add()
            .byDefault()
            .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("monumentDescription", new StringToCodeTypeConverter("eGeodesy/monumentDescription") {});
        converters.registerConverter("geologicCharacteristic", new StringToCodeTypeConverter("eGeodesy/geologicCharacteristic") {});
        converters.registerConverter("faultZonesNearby", new StringToCodeTypeConverter("eGeodesy/faultZonesNearby") {});
        converters.registerConverter(new DateToTimePositionConverter());
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public SiteIdentification to(SiteIdentificationType siteIdType) {
        return mapper.map(siteIdType, SiteIdentification.class);
    }

    /**
     * {@inheritDoc}
     */
    public SiteIdentificationType from(SiteIdentification siteId) {
        return mapper.map(siteId, SiteIdentificationType.class);
    }
}
