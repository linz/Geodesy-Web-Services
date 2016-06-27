package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.DifferentialFromMarker;
import au.gov.ga.geodesy.domain.model.sitelog.SurveyedLocalTie;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.SurveyedLocalTiesType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Reversible mapping between GeodesyML SurveyedLocalTiesType DTO and
 * SurveyedLocalTie site log entity.
 */
public class SurveyedLocalTieMapper implements Iso<SurveyedLocalTiesType, SurveyedLocalTie> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public SurveyedLocalTieMapper() {
        mapperFactory.classMap(SurveyedLocalTiesType.class, SurveyedLocalTie.class)
                .field("tiedMarkerCDPNumber", "tiedMarkerCdpNumber")
                .field("tiedMarkerDOMESNumber", "tiedMarkerDomesNumber")
                .field("differentialComponentsGNSSMarkerToTiedMonumentITRS", "differentialFromMarker")
                .field("localSiteTiesAccuracy", "localSiteTieAccuracy")
                .field("dateMeasured", "dateMeasured")
                .byDefault()
                .register();

        mapperFactory.classMap(SurveyedLocalTiesType.DifferentialComponentsGNSSMarkerToTiedMonumentITRS.class, DifferentialFromMarker.class)
                .field("dx", "dx")
                .field("dy", "dy")
                .field("dz", "dz")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        //converters.registerConverter("differentialConverter", new DifferentialFromMarkerToDifferentialComponentsGNSSMarkerToTiedMonumentITRSConverter() {});
        converters.registerConverter(new InstantToTimePositionConverter());

        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public SurveyedLocalTie to(SurveyedLocalTiesType SurveyedLocalTieType) {
        return mapper.map(SurveyedLocalTieType, SurveyedLocalTie.class);
    }

    /**
     * {@inheritDoc}
     */
    public SurveyedLocalTiesType from(SurveyedLocalTie SurveyedLocalTie) {
        return mapper.map(SurveyedLocalTie, SurveyedLocalTiesType.class);
    }
}
