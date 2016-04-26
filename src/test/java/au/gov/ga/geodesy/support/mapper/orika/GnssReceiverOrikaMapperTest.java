package au.gov.ga.geodesy.support.mapper.orika;

import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;

import net.opengis.gml.v_3_2_1.CodeType;
import net.opengis.gml.v_3_2_1.TimePositionType;

public class GnssReceiverOrikaMapperTest {

    private GnssReceiverOrikaMapper mapper = new GnssReceiverOrikaMapper();

    @Test
    public void testMappingToLogItem() {
        String dateInstalled = "2012-03-24T02:03:23.000Z";

        GnssReceiverType receiver = new GnssReceiverType()
            .withSatelliteSystem(Arrays.asList(
                new CodeType().withValue("GPS").withCodeSpace("eGeodesy/satelliteSystem"),
                new CodeType().withValue("Galileo").withCodeSpace("eGeodesy/satelliteSystem")
            ))
            .withFirmwareVersion("1.2")
            .withManufacturerSerialNumber("123")
            .withElevationCutoffSetting(4)
            .withDateInstalled(timePosition(dateInstalled));

        GnssReceiverLogItem logItem = mapper.mapFromDto(receiver);
        assertEquals(logItem.getFirmwareVersion(), receiver.getFirmwareVersion());
        assertEquals(logItem.getSerialNumber(), receiver.getManufacturerSerialNumber());
        assertEquals(logItem.getElevationCutoffSetting(), String.valueOf(receiver.getElevationCutoffSetting()));
        assertEquals(logItem.getSatelliteSystem(), "GPS,Galileo");
        assertEquals(dateFormat().format(logItem.getDateInstalled()), dateInstalled);

        GnssReceiverType receiverB = mapper.mapToDto(logItem);
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

    private SimpleDateFormat dateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }
}

