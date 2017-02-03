package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.LocalEpisodicEventLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.LocalEpisodicEffectType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML LocalEpisodicEffectType DTO and
 * LocalEpisodicEventLogItem entity.
 */
public class LocalEpisodicEffectMapper implements Iso<LocalEpisodicEffectType, LocalEpisodicEventLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public LocalEpisodicEffectMapper() {
        mapperFactory.classMap(LocalEpisodicEffectType.class, LocalEpisodicEventLogItem.class)
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
    public LocalEpisodicEventLogItem to(LocalEpisodicEffectType localEpisodicEffectType) {
        return mapper.map(localEpisodicEffectType, LocalEpisodicEventLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public LocalEpisodicEffectType from(LocalEpisodicEventLogItem localEpisodicEventLogItem) {
        return mapper.map(localEpisodicEventLogItem, LocalEpisodicEffectType.class);
    }
}
