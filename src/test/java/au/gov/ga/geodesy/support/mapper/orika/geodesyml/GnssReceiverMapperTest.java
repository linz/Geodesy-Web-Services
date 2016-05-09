package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.support.mapper.orika.geodesyml.GnssReceiverMapper;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.IgsReceiverModelCodeType;

import net.opengis.gml.v_3_2_1.CodeType;
import net.opengis.gml.v_3_2_1.TimePositionType;

public class GnssReceiverMapperTest {

    private GnssReceiverMapper mapper = new GnssReceiverMapper();

    @Test
    public void testMappingToLogItem() {
        String dateInstalled = "2012-03-24T02:03:23.000Z";

        GnssReceiverType receiver = new GnssReceiverType()
            .withIgsModelCode((IgsReceiverModelCodeType) new IgsReceiverModelCodeType()
                .withCodeSpace("https://igscb.jpl.nasa.gov/igscb/station/general/rcvr_ant.tab")
                .withCodeList("http://xml.gov.au/icsm/geodesyml/codelists/antenna-receiver-codelists.xml#GeodesyML_GNSSReceiverTypeCode")
                .withCodeListValue("LEICA GRX1200GGPRO")
                .withValue("LEICA GRX1200GGPRO")
            )
            .withSatelliteSystem(Arrays.asList(
                new CodeType().withValue("GPS").withCodeSpace("eGeodesy/satelliteSystem"),
                new CodeType().withValue("Galileo").withCodeSpace("eGeodesy/satelliteSystem")
            ))
            .withFirmwareVersion("1.2")
            .withManufacturerSerialNumber("123")
            .withElevationCutoffSetting(4)
            .withDateInstalled(timePosition(dateInstalled));

        GnssReceiverLogItem logItem = mapper.to(receiver);
        assertEquals(logItem.getType(), receiver.getIgsModelCode().getCodeListValue());
        assertEquals(logItem.getFirmwareVersion(), receiver.getFirmwareVersion());
        assertEquals(logItem.getSerialNumber(), receiver.getManufacturerSerialNumber());
        assertEquals(logItem.getElevationCutoffSetting(), String.valueOf(receiver.getElevationCutoffSetting()));
        assertEquals(logItem.getSatelliteSystem(), "GPS,Galileo");
        assertEquals(dateFormat().format(logItem.getDateInstalled()), dateInstalled);

        GnssReceiverType receiverB = mapper.from(logItem);
        assertEquals(receiverB.getIgsModelCode().getCodeSpace(), "https://igscb.jpl.nasa.gov/igscb/station/general/rcvr_ant.tab");
        assertEquals(receiverB.getIgsModelCode().getCodeList(), "http://xml.gov.au/icsm/geodesyml/codelists/antenna-receiver-codelists.xml#GeodesyML_GNSSReceiverTypeCode");
        assertEquals(receiverB.getIgsModelCode().getCodeListValue(), "LEICA GRX1200GGPRO");
        assertEquals(receiverB.getIgsModelCode().getValue(), "LEICA GRX1200GGPRO");
        assertEquals(receiverB.getFirmwareVersion(), logItem.getFirmwareVersion());
        assertEquals(receiverB.getManufacturerSerialNumber(), logItem.getSerialNumber());
        assertEquals(receiverB.getElevationCutoffSetting(), Double.parseDouble(logItem.getElevationCutoffSetting()));
        assertEquals(receiverB.getSatelliteSystem(), receiver.getSatelliteSystem());
        assertEquals(receiverB.getDateInstalled().getValue().get(0), dateInstalled);
    }

    private TimePositionType timePosition(String date) {
        TimePositionType timePosition = new TimePositionType();
        timePosition.getValue().add(date);
        return timePosition;
    }

    private FastDateFormat dateFormat() {
        return FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("UTC"));
    }
}

