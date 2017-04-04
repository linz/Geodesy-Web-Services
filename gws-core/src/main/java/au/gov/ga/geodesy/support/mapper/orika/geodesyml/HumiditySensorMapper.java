package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.HumiditySensorType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML HumiditySensorType DTO and
 * HumiditySensor site log entity.
 */
public class HumiditySensorMapper implements Iso<HumiditySensorType, HumiditySensorLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public HumiditySensorMapper() {
        mapperFactory.classMap(HumiditySensorType.class, HumiditySensorLogItem.class)
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
        converters.registerConverter(new InstantToTimePositionConverter());
        converters.registerConverter("jaxbElementConverter", new JAXBElementConverter<TimePeriodType, EffectiveDates>() {});
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public HumiditySensorLogItem to(HumiditySensorType humiditySensor) {
        return mapper.map(humiditySensor, HumiditySensorLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public HumiditySensorType from(HumiditySensorLogItem humiditySensorLogItem) {
        return mapper.map(humiditySensorLogItem, HumiditySensorType.class);
    }
}
