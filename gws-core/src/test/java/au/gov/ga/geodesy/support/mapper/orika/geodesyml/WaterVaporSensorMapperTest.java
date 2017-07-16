package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.WaterVaporSensorLogItem;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_4.WaterVaporSensorType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the mapping of a GeodesyML waterVaporSensor element
 * to and from a WaterVaporSensorLogItem domain object.
 */
public class WaterVaporSensorMapperTest extends SensorEquipmentMapperTest {

    @Autowired
    private WaterVaporSensorMapper mapper;

    @Test
    public void testMapping() throws Exception {

        WaterVaporSensorType waterVaporSensorTypeA = getSiteLog().getWaterVaporSensors().get(0).getWaterVaporSensor();

        WaterVaporSensorLogItem logItem = mapper.to(waterVaporSensorTypeA);
        super.testMapping(logItem, waterVaporSensorTypeA, MappingDirection.FROM_DTO_TO_ENTITY);
        assertThat(logItem.getDistanceToAntenna(), equalTo(waterVaporSensorTypeA.getDistanceToAntenna()));
        assertThat(logItem.getNotes(), equalTo(waterVaporSensorTypeA.getNotes()));

        WaterVaporSensorType waterVaporSensorTypeB = mapper.from(logItem);
        super.testMapping(logItem, waterVaporSensorTypeB, MappingDirection.FROM_ENTITY_TO_DTO);
        assertThat(logItem.getDistanceToAntenna().doubleValue(), equalTo(waterVaporSensorTypeB.getDistanceToAntenna()));
        assertThat(logItem.getNotes(), equalTo(waterVaporSensorTypeB.getNotes()));
    }
}
