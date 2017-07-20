package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.WaterVaporSensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.WaterVaporSensorType;

/**
 * Reversible mapping between GeodesyML WaterVaporSensorType DTO and
 * WaterVaporSensor site log entity.
 */
@Component
public class WaterVaporSensorMapper implements Iso<WaterVaporSensorType, WaterVaporSensorLogItem> {

    @Autowired
    private GenericMapper mapper;

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
