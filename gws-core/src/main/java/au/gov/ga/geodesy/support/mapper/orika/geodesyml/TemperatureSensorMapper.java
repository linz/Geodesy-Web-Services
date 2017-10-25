package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_5.TemperatureSensorType;

/**
 * Reversible mapping between GeodesyML TemperatureSensorType DTO and
 * TemperatureSensor site log entity.
 */
@Component
public class TemperatureSensorMapper implements Iso<TemperatureSensorType, TemperatureSensorLogItem> {

    @Autowired
    private GenericMapper mapper;

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
