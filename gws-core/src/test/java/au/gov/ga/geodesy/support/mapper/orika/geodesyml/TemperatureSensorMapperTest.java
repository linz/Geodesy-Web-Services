package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_5.TemperatureSensorType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the mapping of a GeodesyML temperatureSensor element
 * to and from a TemperatureSensorLogItem domain object.
 */
public class TemperatureSensorMapperTest extends SensorEquipmentMapperTest {

    @Autowired
    private TemperatureSensorMapper mapper;

    @Test
    public void testMapping() throws Exception {

        TemperatureSensorType temperatureSensorTypeA = getSiteLog().getTemperatureSensors().get(0).getTemperatureSensor();

        TemperatureSensorLogItem logItem = mapper.to(temperatureSensorTypeA);
        super.testMapping(logItem, temperatureSensorTypeA, MappingDirection.FROM_DTO_TO_ENTITY);
        assertThat(logItem.getAccuracyDegreesCelcius(),
                equalTo(String.valueOf(temperatureSensorTypeA.getAccuracyDegreesCelcius())));
        assertThat(logItem.getAspiration(), equalTo(temperatureSensorTypeA.getAspiration()));
        assertThat(logItem.getDataSamplingInterval(), equalTo(temperatureSensorTypeA.getDataSamplingInterval().toString()));
        assertThat(logItem.getNotes(), equalTo(temperatureSensorTypeA.getNotes()));

        TemperatureSensorType temperatureSensorTypeB = mapper.from(logItem);
        super.testMapping(logItem, temperatureSensorTypeB, MappingDirection.FROM_ENTITY_TO_DTO);
        assertThat(logItem.getAccuracyDegreesCelcius(),
            equalTo(String.valueOf(temperatureSensorTypeB.getAccuracyDegreesCelcius())));
        assertThat(logItem.getAspiration(), equalTo(temperatureSensorTypeB.getAspiration()));
        assertThat(logItem.getDataSamplingInterval(), equalTo(temperatureSensorTypeB.getDataSamplingInterval().toString()));
        assertThat(logItem.getNotes(), equalTo(temperatureSensorTypeB.getNotes()));
    }
}
