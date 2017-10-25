package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_5.PressureSensorType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the mapping of a GeodesyML pressureSensor element
 * to and from a PressureSensorLogItem domain object.
 */
public class PressureSensorMapperTest extends SensorEquipmentMapperTest {

    @Autowired
    private PressureSensorMapper mapper;

    @Test
    public void testMapping() throws Exception {

        PressureSensorType pressureSensorTypeA = getSiteLog().getPressureSensors().get(0).getPressureSensor();

        PressureSensorLogItem logItem = mapper.to(pressureSensorTypeA);
        super.testMapping(logItem, pressureSensorTypeA, MappingDirection.FROM_DTO_TO_ENTITY);
        assertThat(logItem.getAccuracyHPa(),
                equalTo(String.valueOf(pressureSensorTypeA.getAccuracyHPa())));
        assertThat(logItem.getNotes(), equalTo(pressureSensorTypeA.getNotes()));

        PressureSensorType pressureSensorTypeB = mapper.from(logItem);
        super.testMapping(logItem, pressureSensorTypeB, MappingDirection.FROM_ENTITY_TO_DTO);
        assertThat(logItem.getAccuracyHPa(),
            equalTo(String.valueOf(pressureSensorTypeB.getAccuracyHPa())));
        assertThat(logItem.getNotes(), equalTo(pressureSensorTypeB.getNotes()));
    }
}
