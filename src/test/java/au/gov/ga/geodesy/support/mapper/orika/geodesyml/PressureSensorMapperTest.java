package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Tests the mapping of a GeodesyML pressureSensor element
 * to and from a PressureSensorLogItem domain object.
 */
public class PressureSensorMapperTest {

    private PressureSensorMapper mapper = new PressureSensorMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-sensors"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        PressureSensorType pressureSensorTypeA = siteLog.getPressureSensors().get(0).getPressureSensor();

        PressureSensorLogItem logItem = mapper.to(pressureSensorTypeA);
        assertEquals(logItem.getType(),
                pressureSensorTypeA.getType().getValue());
        assertEquals(logItem.getManufacturer(), pressureSensorTypeA.getManufacturer());
        assertEquals(logItem.getSerialNumber(), pressureSensorTypeA.getSerialNumber());
        assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(pressureSensorTypeA.getHeightDiffToAntenna()));
        assertEquals(logItem.getCalibrationDate(),
                GMLDateUtils.stringToDate(pressureSensorTypeA.getCalibrationDate().getValue().get(0)), "uuuu-MM-dd'T'hh:mm:ssX");
        assertEquals(logItem.getEffectiveDates().getFrom(), GMLDateUtils.stringToDate(
                ((TimePeriodType) pressureSensorTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                        .getBeginPosition().getValue().get(0), "uuuu-MM-dd'T'hh:mm:ssX"
        ));
        assertEquals(logItem.getAccuracyHPa(),
                String.valueOf(pressureSensorTypeA.getAccuracyHPa()));
        assertEquals(logItem.getNotes(), pressureSensorTypeA.getNotes());

        PressureSensorType pressureSensorTypeB = mapper.from(logItem);
        assertEquals(logItem.getType(), pressureSensorTypeB.getType().getValue());
        assertEquals(logItem.getManufacturer(), pressureSensorTypeB.getManufacturer());
        assertEquals(logItem.getSerialNumber(), pressureSensorTypeB.getSerialNumber());
        assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(pressureSensorTypeB.getHeightDiffToAntenna()));
        assertEquals(logItem.getCalibrationDate(),
                GMLDateUtils.stringToDate(pressureSensorTypeB.getCalibrationDate().getValue().get(0), "uuuu-MM-dd'T'HH:mm:ss.SSSX"));
        assertEquals(logItem.getEffectiveDates().getFrom(), GMLDateUtils.stringToDate(
                ((TimePeriodType) pressureSensorTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                        .getBeginPosition().getValue().get(0), "uuuu-MM-dd'T'HH:mm:ss.SSSX"
        ));
        assertEquals(logItem.getAccuracyHPa(),
                String.valueOf(pressureSensorTypeB.getAccuracyHPa()));
        assertEquals(logItem.getNotes(), pressureSensorTypeB.getNotes());
    }

}

