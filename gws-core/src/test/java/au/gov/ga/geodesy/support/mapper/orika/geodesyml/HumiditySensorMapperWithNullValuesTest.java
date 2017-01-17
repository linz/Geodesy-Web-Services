package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Tests the mapping of a GeodesyML humiditySensor element
 * to and from a HumiditySensorLogItem domain object.
 *
 * This test uses a specific subset of data that contains null values in numeric elements.
 */
public class HumiditySensorMapperWithNullValuesTest extends SensorEquipmentMapperTest {

    private HumiditySensorMapper mapper = new HumiditySensorMapper();

    @Test
    public void testMapping() throws Exception {

        HumiditySensorType humiditySensorTypeA = getSiteLog().getHumiditySensors().get(0).getHumiditySensor();

        HumiditySensorLogItem logItem = mapper.to(humiditySensorTypeA);

        assertThat(Double.parseDouble(logItem.getHeightDiffToAntenna()), equalTo(humiditySensorTypeA.getHeightDiffToAntenna()));
        assertThat("null Double mapped to null String", logItem.getAccuracyPercentRelativeHumidity() == null);

        HumiditySensorType humiditySensorTypeB = mapper.from(logItem);

        assertThat(humiditySensorTypeB.getHeightDiffToAntenna(), equalTo(Double.parseDouble(logItem.getHeightDiffToAntenna())));
        assertThat("null String mapped to null Double", humiditySensorTypeB.getAccuracyPercentRelativeHumidity() == null);

    }

    public SiteLogType getSiteLog() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("MOBS-null-numerics"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        return siteLog;
    }
}