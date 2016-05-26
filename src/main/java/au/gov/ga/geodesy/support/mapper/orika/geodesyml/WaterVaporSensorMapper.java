package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.WaterVaporSensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.WaterVaporSensorType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML WaterVaporSensorType DTO and
 * WaterVaporSensor site log entity.
 */
public class WaterVaporSensorMapper implements Iso<WaterVaporSensorType, WaterVaporSensorLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public WaterVaporSensorMapper() {
        mapperFactory.classMap(WaterVaporSensorType.class, WaterVaporSensorLogItem.class)
                .fieldMap("type", "type").converter("typeConverter").add()
                .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("jaxbElementConverter").add()
                .field("calibrationDate", "calibrationDate")
                .byDefault()
                .register();

        mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class)
                .field("beginPosition", "from")
                .field("endPosition", "to")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("typeConverter", new StringToCodeTypeConverter("eGeodesy/type") {});
        converters.registerConverter(new DateToTimePositionConverter());
        converters.registerConverter("jaxbElementConverter", new JAXBElementConverter<TimePeriodType, EffectiveDates>() {});
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public WaterVaporSensorLogItem to(WaterVaporSensorType waterVaporSensor) {
        return mapper.map(waterVaporSensor, WaterVaporSensorLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public WaterVaporSensorType from(WaterVaporSensorLogItem waterVaporSensorLogItem) {
        return mapper.map(waterVaporSensorLogItem, WaterVaporSensorType.class);
    }
}
