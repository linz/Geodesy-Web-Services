package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.FrequencyStandardLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardType;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML FrequencyStandardType DTO and
 * FreqeyncyStandard site log entity.
 */
public class FrequencyStandardMapper implements Iso<FrequencyStandardType, FrequencyStandardLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public FrequencyStandardMapper() {
        mapperFactory.classMap(FrequencyStandardType.class, FrequencyStandardLogItem.class)
            .fieldMap("standardType", "type").converter("typeConverter").add()
            .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("jaxbElementConverter").add()
            .byDefault()
            .register();

        mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class)
            .field("beginPosition", "from")
            .field("endPosition", "to")
            .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("typeConverter", new StringToCodeTypeConverter("eGeodesy/frequencyStandardType") {});
        converters.registerConverter("jaxbElementConverter", new JAXBElementConverter<TimePeriodType, EffectiveDates>() {});
        converters.registerConverter(new DateToTimePositionConverter());
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public FrequencyStandardLogItem to(FrequencyStandardType frequencyStandard) {
        return mapper.map(frequencyStandard, FrequencyStandardLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public FrequencyStandardType from(FrequencyStandardLogItem frequencyStandardLogItem) {
        return mapper.map(frequencyStandardLogItem, FrequencyStandardType.class);
    }
}
