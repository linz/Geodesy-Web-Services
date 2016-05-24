package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML TemperatureSensorType DTO and
 * TemperatureSensor site log entity.
 */
public class TemperatureSensorMapper implements Iso<TemperatureSensorType, TemperatureSensorLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public TemperatureSensorMapper() {
        mapperFactory.classMap(TemperatureSensorType.class, TemperatureSensorLogItem.class)
                .fieldMap("type", "type").converter("typeConverter").add()
                .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("jaxbElementConverter").add()
                .field("calibrationDate", "calibrationDate")
                .field("dataSamplingInterval", "dataSamplingInterval")
                .byDefault()
                .register();

        mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class)
                .field("beginPosition", "from")
                .field("endPosition", "to")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("typeConverter", new StringToCodeTypeConverter("eGeodesy/type") {});
        converters.registerConverter(new DateToTimePositionConverter());
        converters.registerConverter(new BigDecimalToStringConverter());
        converters.registerConverter("jaxbElementConverter", new JAXBElementConverter<TimePeriodType, EffectiveDates>() {});
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public TemperatureSensorLogItem to(TemperatureSensorType temperatureSensor) {
        return mapper.map(temperatureSensor, TemperatureSensorLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public TemperatureSensorType from(TemperatureSensorLogItem temperatureSensorLogItem) {
        return mapper.map(temperatureSensorLogItem, TemperatureSensorType.class);
    }
}
