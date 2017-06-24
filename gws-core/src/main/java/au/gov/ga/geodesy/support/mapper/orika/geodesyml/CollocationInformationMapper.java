package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.CollocationInformationLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.CollocationInformationType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML CollocationInformationType DTO and
 * CollocationInformation site log entity.
 */
public class CollocationInformationMapper implements Iso<CollocationInformationType, CollocationInformationLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public CollocationInformationMapper() {
        mapperFactory.classMap(CollocationInformationType.class, CollocationInformationLogItem.class)
                .fieldMap("instrumentationType", "instrumentType").converter("instrumentTypeConverter").add()
                .fieldMap("status", "status").converter("statusTypeConverter").add()
                .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("jaxbElemConverter").add()
                .byDefault()
                .register();

        mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class)
                .field("beginPosition", "from")
                .field("endPosition", "to")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("instrumentTypeConverter", new StringToCodeTypeConverter("eGeodesy/instrumentType") {});
        converters.registerConverter("statusTypeConverter", new StringToCodeTypeConverter("eGeodesy/status") {});
        converters.registerConverter("jaxbElemConverter", new JAXBElementConverter<TimePeriodType, EffectiveDates>() {});
        converters.registerConverter(new InstantToTimePositionConverter());

        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public CollocationInformationLogItem to(CollocationInformationType collocationInformationType) {
        return mapper.map(collocationInformationType, CollocationInformationLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public CollocationInformationType from(CollocationInformationLogItem collocationInformation) {
        return mapper.map(collocationInformation, CollocationInformationType.class);
    }
}
