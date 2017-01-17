package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Common tests for mapping of sensor equipment XML types to domain objects
 */
public class SensorEquipmentMapperTest {

    protected GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    public void testMapping(SensorEquipmentLogItem logItem, BaseSensorEquipmentType sensorType, MappingDirection mappingDirection) {

        DateTimeFormatter outputFormat = dateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        if (mappingDirection == MappingDirection.FROM_DTO_TO_ENTITY) {

            assertThat(logItem.getType(), equalTo(sensorType.getType().getValue()));
            assertThat(logItem.getManufacturer(), equalTo(sensorType.getManufacturer()));
            assertThat(logItem.getSerialNumber(), equalTo(sensorType.getSerialNumber()));
            assertThat(logItem.getHeightDiffToAntenna(), equalTo(String.valueOf(sensorType.getHeightDiffToAntenna())));
            assertThat(outputFormat.format(logItem.getCalibrationDate()),
                equalTo(GMLDateUtils.stringToDateToStringMultiParsers(sensorType.getCalibrationDate().getValue().get(0))));

            String xmlEffectiveDateFrom = ((TimePeriodType) sensorType.getValidTime().getAbstractTimePrimitive().getValue())
                    .getBeginPosition().getValue().get(0);
            assertThat(outputFormat.format(logItem.getEffectiveDates().getFrom()),
                equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom)));
        } else {


            assertThat(sensorType.getType().getValue(), equalTo(logItem.getType()));
            assertThat(sensorType.getManufacturer(), equalTo(logItem.getManufacturer()));
            assertThat(sensorType.getSerialNumber(), equalTo(logItem.getSerialNumber()));
            assertThat(String.valueOf(sensorType.getHeightDiffToAntenna()), equalTo(logItem.getHeightDiffToAntenna()));
            assertThat(GMLDateUtils.stringToDateToStringMultiParsers(sensorType.getCalibrationDate().getValue().get(0)),
                equalTo(outputFormat.format(logItem.getCalibrationDate())));
            String xmlEffectiveDateFrom = ((TimePeriodType) sensorType.getValidTime().getAbstractTimePrimitive().getValue())
                    .getBeginPosition().getValue().get(0);
            assertThat(
                    GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom),
                equalTo(outputFormat.format(logItem.getEffectiveDates().getFrom())));
        }
    }

    public SiteLogType getSiteLog() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("MOBS-sensors"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        return siteLog;
    }

    protected static DateTimeFormatter dateFormat(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
    }
}
