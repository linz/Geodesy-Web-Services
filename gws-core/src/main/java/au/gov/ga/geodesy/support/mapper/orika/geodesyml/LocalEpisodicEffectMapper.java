package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.LocalEpisodicEffectLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.LocalEpisodicEffectType;

/**
 * Reversible mapping between GeodesyML LocalEpisodicEffectType DTO and
 * LocalEpisodicEffectLogItem entity.
 */
@Component
public class LocalEpisodicEffectMapper implements Iso<LocalEpisodicEffectType, LocalEpisodicEffectLogItem> {

    @Autowired
    private GenericMapper mapper;

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
