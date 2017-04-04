package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.OtherInstrumentationLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.OtherInstrumentationType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML OtherInstrumentationType DTO and
 * OtherInstrumentationLogItem site log entity.
 */
public class OtherInstrumentationMapper implements Iso<OtherInstrumentationType, OtherInstrumentationLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public OtherInstrumentationMapper() {
        mapperFactory.classMap(OtherInstrumentationType.class, OtherInstrumentationLogItem.class)
                .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("jaxbElementConverter").add()
                .byDefault()
                .register();

        mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class)
                .field("beginPosition", "from")
                .field("endPosition", "to")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter(new InstantToTimePositionConverter());
        converters.registerConverter("jaxbElementConverter", new JAXBElementConverter<TimeInstantType, EffectiveDates>() {});
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public OtherInstrumentationLogItem to(OtherInstrumentationType otherInstrumentationType) {
        return mapper.map(otherInstrumentationType, OtherInstrumentationLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public OtherInstrumentationType from(OtherInstrumentationLogItem otherInstrumentationLogItem) {
        return mapper.map(otherInstrumentationLogItem, OtherInstrumentationType.class);
    }
}
