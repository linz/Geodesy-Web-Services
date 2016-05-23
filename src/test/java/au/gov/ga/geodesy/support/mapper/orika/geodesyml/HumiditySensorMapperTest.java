package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Tests the mapping of a GeodesyML humiditySensor element
 * to and from a HumiditySensorLogItem domain object.
 */
public class HumiditySensorMapperTest {

    private HumiditySensorMapper mapper = new HumiditySensorMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        HumiditySensorType humiditySensorTypeA = siteLog.getHumiditySensors().get(0).getHumiditySensor();

        HumiditySensorLogItem logItem = mapper.to(humiditySensorTypeA);
        assertEquals(logItem.getType(),
                humiditySensorTypeA.getType().getValue());
        assertEquals(logItem.getManufacturer(), humiditySensorTypeA.getManufacturer());
        assertEquals(logItem.getSerialNumber(), humiditySensorTypeA.getSerialNumber());
        assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(humiditySensorTypeA.getHeightDiffToAntenna()));
        assertEquals(logItem.getCalibrationDate(),
                parseDate("yyyy-MM-dd", humiditySensorTypeA.getCalibrationDate().getValue().get(0)));
        assertEquals(logItem.getEffectiveDates().getFrom(), parseDate("yyyy-MM-ddX",
                ((TimePeriodType) humiditySensorTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                        .getBeginPosition().getValue().get(0)
        ));
        assertEquals(logItem.getAccuracyPercentRelativeHumidity(),
                String.valueOf(humiditySensorTypeA.getAccuracyPercentRelativeHumidity()));
        assertEquals(logItem.getAspiration(), humiditySensorTypeA.getAspiration());
        assertEquals(logItem.getNotes(), humiditySensorTypeA.getNotes());

        HumiditySensorType humiditySensorTypeB = mapper.from(logItem);
        assertEquals(logItem.getType(), humiditySensorTypeB.getType().getValue());
        assertEquals(logItem.getManufacturer(), humiditySensorTypeB.getManufacturer());
        assertEquals(logItem.getSerialNumber(), humiditySensorTypeB.getSerialNumber());
        assertEquals(logItem.getHeightDiffToAntenna(), String.valueOf(humiditySensorTypeB.getHeightDiffToAntenna()));
        assertEquals(logItem.getCalibrationDate(),
                parseDate("yyyy-MM-dd", humiditySensorTypeB.getCalibrationDate().getValue().get(0)));
        assertEquals(logItem.getEffectiveDates().getFrom(), parseDate("yyyy-MM-dd'T'HH:mm:ss.SSSX",
                ((TimePeriodType) humiditySensorTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                        .getBeginPosition().getValue().get(0)
        ));
        assertEquals(logItem.getAccuracyPercentRelativeHumidity(),
                String.valueOf(humiditySensorTypeB.getAccuracyPercentRelativeHumidity()));
        assertEquals(logItem.getAspiration(), humiditySensorTypeB.getAspiration());
        assertEquals(logItem.getNotes(), humiditySensorTypeB.getNotes());
    }

    private Date parseDate(String pattern, String str) throws ParseException {
        return FastDateFormat.getInstance(pattern, TimeZone.getTimeZone("UTC")).parse(str);
    }
}

