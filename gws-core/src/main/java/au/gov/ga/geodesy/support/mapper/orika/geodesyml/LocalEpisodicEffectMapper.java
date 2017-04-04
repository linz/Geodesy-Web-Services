package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.LocalEpisodicEffectLogItem;
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
 * LocalEpisodicEffectLogItem entity.
 */
public class LocalEpisodicEffectMapper implements Iso<LocalEpisodicEffectType, LocalEpisodicEffectLogItem> {

	private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
	private MapperFacade mapper;

	public LocalEpisodicEffectMapper() {
		mapperFactory.classMap(LocalEpisodicEffectType.class, LocalEpisodicEffectLogItem.class)
				.fieldMap("validTime.abstractTimePrimitive", "effectiveDates").converter("effectiveDatesConverter")
				.add().byDefault().register();

		mapperFactory.classMap(TimePeriodType.class, EffectiveDates.class).field("beginPosition", "from")
				.field("endPosition", "to").register();

		ConverterFactory converters = mapperFactory.getConverterFactory();
		converters.registerConverter(new InstantToTimePositionConverter());
		converters.registerConverter("effectiveDatesConverter",
				new JAXBElementConverter<TimeInstantType, EffectiveDates>() {
				});
		mapper = mapperFactory.getMapperFacade();
	}

	/**
	 * {@inheritDoc}
	 */
	public LocalEpisodicEffectLogItem to(LocalEpisodicEffectType localEpisodicEffectType) {
		return mapper.map(localEpisodicEffectType, LocalEpisodicEffectLogItem.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public LocalEpisodicEffectType from(LocalEpisodicEffectLogItem localEpisodicEffectLogItem) {
		return mapper.map(localEpisodicEffectLogItem, LocalEpisodicEffectType.class);
	}
}
