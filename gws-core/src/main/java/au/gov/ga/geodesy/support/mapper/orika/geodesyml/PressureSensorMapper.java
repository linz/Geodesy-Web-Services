package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.PressureSensorType;

/**
 * Reversible mapping between GeodesyML PressureSensorType DTO and
 * PressureSensor site log entity.
 */
@Component
public class PressureSensorMapper implements Iso<PressureSensorType, PressureSensorLogItem> {

    @Autowired
    private GenericMapper mapper;

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
