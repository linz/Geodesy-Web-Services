package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.MultipathSourceLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.BasePossibleProblemSourcesType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML BasePossibleProblemSourcesType DTO and
 * MultipathSourceLogItem entity.
 */
public class MultipathSourcesMapper implements Iso<BasePossibleProblemSourcesType, MultipathSourceLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public MultipathSourcesMapper() {
        mapperFactory.classMap(BasePossibleProblemSourcesType.class, MultipathSourceLogItem.class)
                .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("effectiveDatesConverter").add()
                .field("possibleProblemSources", "possibleProblemSource")
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
    public MultipathSourceLogItem to(BasePossibleProblemSourcesType basePossibleProblemSourcesType) {
        return mapper.map(basePossibleProblemSourcesType, MultipathSourceLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public BasePossibleProblemSourcesType from(MultipathSourceLogItem multipathSourceLogItem) {
        return mapper.map(multipathSourceLogItem, BasePossibleProblemSourcesType.class);
    }
}
