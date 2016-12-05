package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML PressureSensorType DTO and
 * PressureSensor site log entity.
 */
public class PressureSensorMapper implements Iso<PressureSensorType, PressureSensorLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public PressureSensorMapper() {
        mapperFactory.classMap(PressureSensorType.class, PressureSensorLogItem.class)
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
    public PressureSensorLogItem to(PressureSensorType pressureSensor) {
        return mapper.map(pressureSensor, PressureSensorLogItem.class);
    }

    /**
     * {@inheritDoc}
     */
    public PressureSensorType from(PressureSensorLogItem pressureSensorLogItem) {
        return mapper.map(pressureSensorLogItem, PressureSensorType.class);
    }
}
