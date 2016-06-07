package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorType;

/**
 * Tests the mapping of a GeodesyML temperatureSensor element
 * to and from a TemperatureSensorLogItem domain object.
 */
public class TemperatureSensorMapperTest extends SensorEquipmentMapperTest {

    private TemperatureSensorMapper mapper = new TemperatureSensorMapper();

    @Test
    public void testMapping() throws Exception {

        TemperatureSensorType temperatureSensorTypeA = getSiteLog().getTemperatureSensors().get(0).getTemperatureSensor();

        TemperatureSensorLogItem logItem = mapper.to(temperatureSensorTypeA);
        super.testMapping(logItem, temperatureSensorTypeA, MappingDirection.FROM_DTO_TO_ENTITY);
        assertEquals(logItem.getAccuracyDegreesCelcius(),
                String.valueOf(temperatureSensorTypeA.getAccuracyDegreesCelcius()));
        assertEquals(logItem.getAspiration(), temperatureSensorTypeA.getAspiration());
        assertEquals(logItem.getDataSamplingInterval(), temperatureSensorTypeA.getDataSamplingInterval().toString());
        assertEquals(logItem.getNotes(), temperatureSensorTypeA.getNotes());

        TemperatureSensorType temperatureSensorTypeB = mapper.from(logItem);
        super.testMapping(logItem, temperatureSensorTypeB, MappingDirection.FROM_ENTITY_TO_DTO);
        assertEquals(logItem.getAccuracyDegreesCelcius(),
                String.valueOf(temperatureSensorTypeB.getAccuracyDegreesCelcius()));
        assertEquals(logItem.getAspiration(), temperatureSensorTypeB.getAspiration());
        assertEquals(logItem.getDataSamplingInterval(), temperatureSensorTypeB.getDataSamplingInterval().toString());
        assertEquals(logItem.getNotes(), temperatureSensorTypeB.getNotes());
    }
}