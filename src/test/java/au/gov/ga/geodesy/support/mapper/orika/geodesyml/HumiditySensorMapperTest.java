package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;

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
        assertEquals(logItem.getAspiration(), humiditySensorTypeA.getAspiration());
        assertEquals(logItem.getNotes(), humiditySensorTypeA.getNotes());

        HumiditySensorType humiditySensorTypeB = mapper.from(logItem);
        super.testMapping(logItem, humiditySensorTypeB, MappingDirection.FROM_ENTITY_TO_DTO);
        assertEquals(logItem.getAspiration(), humiditySensorTypeB.getAspiration());
        assertEquals(logItem.getNotes(), humiditySensorTypeB.getNotes());
    }

}