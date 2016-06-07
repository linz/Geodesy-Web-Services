package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static afu.plume.TaskManager.format;
import static org.testng.Assert.assertEquals;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import au.gov.ga.geodesy.domain.model.sitelog.SensorEquipmentLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.ga.geodesy.support.utils.MappingDirection;
import au.gov.xml.icsm.geodesyml.v_0_3.BaseSensorEquipmentType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Common tests for mapping of sensor equipment XML types to domain objects
 */
public class SensorEquipmentMapperTest {

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    public void testMapping(SensorEquipmentLogItem logItem, BaseSensorEquipmentType sensorType, MappingDirection mappingDirection) {

        DateTimeFormatter format = null;

        if (mappingDirection == MappingDirection.FROM_DTO_TO_ENTITY) {
            format = dateFormat("yyyy-MM-dd'T'HH:mm:ssX");

            assertEquals(logItem.getType(), sensorType.getType().getValue());
            assertEquals(logItem.getManufacturer(), sensorType.getManufacturer());
            assertEquals(logItem.getSerialNumber(), sensorType.getSerialNumber());
            assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(sensorType.getHeightDiffToAntenna()));
            assertEquals(format.format(logItem.getCalibrationDate()),
                    GMLDateUtils.stringToDateToString(sensorType.getCalibrationDate().getValue().get(0), format));
            assertEquals(format.format(logItem.getEffectiveDates().getFrom()),
                    ((TimePeriodType) sensorType.getValidTime().getAbstractTimePrimitive().getValue())
                            .getBeginPosition().getValue().get(0));
        } else {
            format = dateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            assertEquals(sensorType.getType().getValue(), logItem.getType());
            assertEquals(sensorType.getManufacturer(), logItem.getManufacturer());
            assertEquals(sensorType.getSerialNumber(), logItem.getSerialNumber());
            assertEquals(String.valueOf(sensorType.getHeightDiffToAntenna()), logItem.getHeightDiffToAntenna());
            assertEquals(GMLDateUtils.stringToDateToString(sensorType.getCalibrationDate().getValue().get(0), format),
                    format.format(logItem.getCalibrationDate()));
            assertEquals(
                    ((TimePeriodType) sensorType.getValidTime().getAbstractTimePrimitive().getValue())
                            .getBeginPosition().getValue().get(0), format.format(logItem.getEffectiveDates().getFrom()));
        }
    }

    public SiteLogType getSiteLog() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-sensors"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        return siteLog;
    }

    private DateTimeFormatter dateFormat(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
    }
}