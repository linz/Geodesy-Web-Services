package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the mapping of a GeodesyML humiditySensor element
 * to and from a HumiditySensorLogItem domain object.
 */
public class HumiditySensorMapperTest extends SensorEquipmentMapperTest {

    private HumiditySensorMapper mapper = new HumiditySensorMapper();

    @Test
    public void testMapping() throws Exception {

        HumiditySensorType humiditySensorTypeA = getSiteLog().getHumiditySensors().get(0).getHumiditySensor();

        HumiditySensorLogItem logItem = mapper.to(humiditySensorTypeA);
        super.testMapping(logItem, humiditySensorTypeA, MappingDirection.FROM_DTO_TO_ENTITY);
        assertThat(logItem.getAspiration(), equalTo(humiditySensorTypeA.getAspiration()));
        assertThat(logItem.getNotes(), equalTo(humiditySensorTypeA.getNotes()));

        HumiditySensorType humiditySensorTypeB = mapper.from(logItem);
        super.testMapping(logItem, humiditySensorTypeB, MappingDirection.FROM_ENTITY_TO_DTO);
        assertThat(logItem.getAspiration(), equalTo(humiditySensorTypeB.getAspiration()));
        assertThat(logItem.getNotes(), equalTo(humiditySensorTypeB.getNotes()));
    }

}