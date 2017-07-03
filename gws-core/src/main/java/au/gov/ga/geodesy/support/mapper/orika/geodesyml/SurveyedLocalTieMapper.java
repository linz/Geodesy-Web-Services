package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.DifferentialFromMarker;
import au.gov.ga.geodesy.domain.model.sitelog.SurveyedLocalTieLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.SurveyedLocalTieType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Reversible mapping between GeodesyML SurveyedLocalTiesType DTO and
 * SurveyedLocalTie site log entity.
 */
public class SurveyedLocalTieMapper implements Iso<SurveyedLocalTieType, SurveyedLocalTieLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public SurveyedLocalTieMapper() {
        mapperFactory.classMap(SurveyedLocalTieType.class, SurveyedLocalTieLogItem.class)
                .field("tiedMarkerCDPNumber", "tiedMarkerCdpNumber")
                .field("tiedMarkerDOMESNumber", "tiedMarkerDomesNumber")
                .field("differentialComponentsGNSSMarkerToTiedMonumentITRS", "differentialFromMarker")
                .field("localSiteTiesAccuracy", "localSiteTieAccuracy")
                .field("dateMeasured", "dateMeasured")
                .byDefault()
                .register();

        mapperFactory.classMap(SurveyedLocalTieType.DifferentialComponentsGNSSMarkerToTiedMonumentITRS.class, DifferentialFromMarker.class)
                .field("dx", "dx")
                .field("dy", "dy")
                .field("dz", "dz")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter(new InstantToTimePositionConverter());

        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public SurveyedLocalTieLogItem to(SurveyedLocalTieType SurveyedLocalTieType) {
        return mapper.map(SurveyedLocalTieType, SurveyedLocalTieLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public SurveyedLocalTieType from(SurveyedLocalTieLogItem SurveyedLocalTie) {
        return mapper.map(SurveyedLocalTie, SurveyedLocalTieType.class);
    }
}
