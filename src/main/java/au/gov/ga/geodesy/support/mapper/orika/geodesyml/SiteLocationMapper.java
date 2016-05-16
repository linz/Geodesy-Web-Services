package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.ApproximatePosition;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLocation;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Reversible mapping between GeodesyML SiteLocationType DTO and
 * SiteLocation site log entity.
 */
public class SiteLocationMapper implements Iso<SiteLocationType, SiteLocation> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapper;

    public SiteLocationMapper() {
        mapperFactory.classMap(SiteLocationType.class, SiteLocation.class)
            .fieldMap("countryCodeISO", "country").add()
            .fieldMap("tectonicPlate", "tectonicPlate").converter("tectonicPlate").add()
            .fieldMap("approximatePositionITRF", "approximatePosition").add()
            .byDefault()
            .register();

        mapperFactory.classMap(SiteLocationType.ApproximatePositionITRF.class, ApproximatePosition.class)
            .field("XCoordinateInMeters", "itrfX")
            .field("YCoordinateInMeters", "itrfY")
            .field("ZCoordinateInMeters", "itrfZ")
            .field("elevationMEllips", "elevationGrs80")
            .byDefault()
            .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("tectonicPlate", new StringToCodeTypeConverter("eGeodesy/tectonicPlate"));
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public SiteLocation to(SiteLocationType siteLocType) {
        return mapper.map(siteLocType, SiteLocation.class);
    }

    /**
     * {@inheritDoc}
     */
    public SiteLocationType from(SiteLocation siteLoc) {
        return mapper.map(siteLoc, SiteLocationType.class);
    }
}
