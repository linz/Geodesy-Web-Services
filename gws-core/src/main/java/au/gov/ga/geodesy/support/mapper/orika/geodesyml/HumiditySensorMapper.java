package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_5.HumiditySensorType;

/**
 * Reversible mapping between GeodesyML HumiditySensorType DTO and
 * HumiditySensor site log entity.
 */
@Component
public class HumiditySensorMapper implements Iso<HumiditySensorType, HumiditySensorLogItem> {

    @Autowired
    private GenericMapper mapper;

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
