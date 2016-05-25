package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.WaterVaporSensorLogItem;
import au.gov.xml.icsm.geodesyml.v_0_3.WaterVaporSensorType;

/**
 * Tests the mapping of a GeodesyML waterVaporSensor element
 * to and from a WaterVaporSensorLogItem domain object.
 */
public class WaterVaporSensorMapperTest extends SensorEquipmentMapperTest {

    private WaterVaporSensorMapper mapper = new WaterVaporSensorMapper();

    @Test
    public void testMapping() throws Exception {

        WaterVaporSensorType waterVaporSensorTypeA = getSiteLog().getWaterVaporSensors().get(0).getWaterVaporSensor();

        WaterVaporSensorLogItem logItem = mapper.to(waterVaporSensorTypeA);

        super.testMapping(logItem, waterVaporSensorTypeA);
        assertEquals(logItem.getDistanceToAntenna(), waterVaporSensorTypeA.getDistanceToAntenna());
        assertEquals(logItem.getNotes(), waterVaporSensorTypeA.getNotes());

        WaterVaporSensorType waterVaporSensorTypeB = mapper.from(logItem);

        super.testMapping(logItem, waterVaporSensorTypeB);
        assertEquals(logItem.getDistanceToAntenna().doubleValue(), waterVaporSensorTypeB.getDistanceToAntenna());
        assertEquals(logItem.getNotes(), waterVaporSensorTypeB.getNotes());
    }
}

