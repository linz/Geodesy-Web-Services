package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.SignalObstructionLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.BasePossibleProblemSourceType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML BasePossibleProblemSourcesType DTO and
 * SignalObstructionLogItem entity.
 */
public class SignalObstructionMapper implements Iso<BasePossibleProblemSourceType, SignalObstructionLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public SignalObstructionMapper() {
        mapperFactory.classMap(BasePossibleProblemSourceType.class, SignalObstructionLogItem.class)
                .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("effectiveDatesConverter").add()
                .byDefault()
                .register();

        mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class)
                .field("beginPosition", "from")
                .field("endPosition", "to")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter(new InstantToTimePositionConverter());
        converters.registerConverter("effectiveDatesConverter", new JAXBElementConverter<TimeInstantType, EffectiveDates>() {});
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public SignalObstructionLogItem to(BasePossibleProblemSourceType signalObstructionPropertyType) {
        return mapper.map(signalObstructionPropertyType, SignalObstructionLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public BasePossibleProblemSourceType from(SignalObstructionLogItem signalObstructionLogItem) {
        return mapper.map(signalObstructionLogItem, BasePossibleProblemSourceType.class);
    }
}
