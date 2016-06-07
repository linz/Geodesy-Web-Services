package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorType;

/**
 * Tests the mapping of a GeodesyML pressureSensor element
 * to and from a PressureSensorLogItem domain object.
 */
public class PressureSensorMapperTest extends SensorEquipmentMapperTest {

    private PressureSensorMapper mapper = new PressureSensorMapper();

    @Test
    public void testMapping() throws Exception {

        PressureSensorType pressureSensorTypeA = getSiteLog().getPressureSensors().get(0).getPressureSensor();

        PressureSensorLogItem logItem = mapper.to(pressureSensorTypeA);
        super.testMapping(logItem, pressureSensorTypeA, MappingDirection.FROM_DTO_TO_ENTITY);
        assertEquals(logItem.getAccuracyHPa(),
                String.valueOf(pressureSensorTypeA.getAccuracyHPa()));
        assertEquals(logItem.getNotes(), pressureSensorTypeA.getNotes());

        PressureSensorType pressureSensorTypeB = mapper.from(logItem);
        super.testMapping(logItem, pressureSensorTypeB, MappingDirection.FROM_ENTITY_TO_DTO);
        assertEquals(logItem.getAccuracyHPa(),
                String.valueOf(pressureSensorTypeB.getAccuracyHPa()));
        assertEquals(logItem.getNotes(), pressureSensorTypeB.getNotes());
    }
}