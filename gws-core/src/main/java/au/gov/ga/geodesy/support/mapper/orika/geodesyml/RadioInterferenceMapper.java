package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.RadioInterference;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.RadioInterferenceType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Reversible mapping between GeodesyML RadioInterferencesType DTO and
 * RadioInterference site log entity.
 */
public class RadioInterferenceMapper implements Iso<RadioInterferenceType, RadioInterference> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public RadioInterferenceMapper() {
        mapperFactory.classMap(RadioInterferenceType.class, RadioInterference.class)
                .fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("effectiveDatesConverter").add()
                .byDefault()
                .register();

        mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class)
                .field("beginPosition", "from")
                .field("endPosition", "to")
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter(new InstantToTimePositionConverter());
        converters.registerConverter("effectiveDatesConverter", new JAXBElementConverter<TimeInstantType, EffectiveDates>() {
        });
        mapper = mapperFactory.getMapperFacade();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public RadioInterference to(RadioInterferenceType radioInterferenceType) {
        return mapper.map(radioInterferenceType, RadioInterference.class);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public RadioInterferenceType from(RadioInterference radioInterference) {
        return mapper.map(radioInterference, RadioInterferenceType.class);
    }
}
