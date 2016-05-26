package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import au.gov.ga.geodesy.domain.model.sitelog.SensorEquipmentLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.BaseSensorEquipmentType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Common tests for mapping of sensor equipment XML types to domain objects
 */
public class SensorEquipmentMapperTest {

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    public void testMapping(SensorEquipmentLogItem logItem, BaseSensorEquipmentType sensorType) {
        assertEquals(logItem.getType(),
                sensorType.getType().getValue());
        assertEquals(logItem.getManufacturer(), sensorType.getManufacturer());
        assertEquals(logItem.getSerialNumber(), sensorType.getSerialNumber());
        assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(sensorType.getHeightDiffToAntenna()));
        assertEquals(logItem.getCalibrationDate(),
                GMLDateUtils.stringToDateMultiParsers(sensorType.getCalibrationDate().getValue().get(0)));
        assertEquals(logItem.getEffectiveDates().getFrom(), GMLDateUtils.stringToDateMultiParsers(
                ((TimePeriodType) sensorType.getValidTime().getAbstractTimePrimitive().getValue())
                        .getBeginPosition().getValue().get(0)
        ));
    }

    public SiteLogType getSiteLog() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-sensors"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        return siteLog;
    }
}
