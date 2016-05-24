package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Tests the mapping of a GeodesyML temperatureSensor element
 * to and from a TemperatureSensorLogItem domain object.
 */
public class TemperatureSensorMapperTest {

    private TemperatureSensorMapper mapper = new TemperatureSensorMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-sensors"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        TemperatureSensorType temperatureSensorTypeA = siteLog.getTemperatureSensors().get(0).getTemperatureSensor();

        TemperatureSensorLogItem logItem = mapper.to(temperatureSensorTypeA);
        assertEquals(logItem.getType(),
                temperatureSensorTypeA.getType().getValue());
        assertEquals(logItem.getManufacturer(), temperatureSensorTypeA.getManufacturer());
        assertEquals(logItem.getSerialNumber(), temperatureSensorTypeA.getSerialNumber());
        assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(temperatureSensorTypeA.getHeightDiffToAntenna()));
        assertEquals(logItem.getCalibrationDate(),
                parseDate("yyyy-MM-dd'T'HH:mm:ssX", temperatureSensorTypeA.getCalibrationDate().getValue().get(0)));
        assertEquals(logItem.getEffectiveDates().getFrom(), parseDate("yyyy-MM-dd'T'hh:mm:ssX",
                ((TimePeriodType) temperatureSensorTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                        .getBeginPosition().getValue().get(0)
        ));
        assertEquals(logItem.getAccuracyDegreesCelcius(),
                String.valueOf(temperatureSensorTypeA.getAccuracyDegreesCelcius()));
        assertEquals(logItem.getAspiration(), temperatureSensorTypeA.getAspiration());
        assertEquals(logItem.getDataSamplingInterval(), temperatureSensorTypeA.getDataSamplingInterval().toString());
        assertEquals(logItem.getNotes(), temperatureSensorTypeA.getNotes());

        TemperatureSensorType temperatureSensorTypeB = mapper.from(logItem);
        assertEquals(logItem.getType(), temperatureSensorTypeB.getType().getValue());
        assertEquals(logItem.getManufacturer(), temperatureSensorTypeB.getManufacturer());
        assertEquals(logItem.getSerialNumber(), temperatureSensorTypeB.getSerialNumber());
        assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(temperatureSensorTypeB.getHeightDiffToAntenna()));
        assertEquals(logItem.getCalibrationDate(),
                parseDate("yyyy-MM-dd'T'HH:mm:ss.SSSX", temperatureSensorTypeB.getCalibrationDate().getValue().get(0)));
        assertEquals(logItem.getEffectiveDates().getFrom(), parseDate("yyyy-MM-dd'T'HH:mm:ss.SSSX",
                ((TimePeriodType) temperatureSensorTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                        .getBeginPosition().getValue().get(0)
        ));
        assertEquals(logItem.getAccuracyDegreesCelcius(),
                String.valueOf(temperatureSensorTypeB.getAccuracyDegreesCelcius()));
        assertEquals(logItem.getAspiration(), temperatureSensorTypeB.getAspiration());
        assertEquals(logItem.getDataSamplingInterval(), temperatureSensorTypeB.getDataSamplingInterval().toString());
        assertEquals(logItem.getNotes(), temperatureSensorTypeB.getNotes());
    }

    private Date parseDate(String pattern, String str) throws ParseException {
        return FastDateFormat.getInstance(pattern, TimeZone.getTimeZone("UTC")).parse(str);
    }
}

