package au.gov.ga.geodesy.support.mapper.orika;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;

import net.opengis.gml.v_3_2_1.CodeType;

public class GnssReceiverOrikaMapperTest {

    private GnssReceiverOrikaMapper mapper = new GnssReceiverOrikaMapper();

    @Test
    public void testMappingToLogItem() {
        GnssReceiverType receiver = new GnssReceiverType();

        CodeType gps = new CodeType();
        gps.setValue("GPS");
        gps.setCodeSpace("eGeodesy/satelliteSystem");

        CodeType galileo = new CodeType();
        galileo.setValue("Galileo");
        galileo.setCodeSpace("eGeodesy/satelliteSystem");

        List<CodeType> satelliteSystems = Arrays.asList(gps, galileo);
        receiver.setSatelliteSystem(satelliteSystems);

        receiver.setFirmwareVersion("1.2");
        receiver.setManufacturerSerialNumber("123");
        GnssReceiverLogItem logItem = mapper.mapFromDto(receiver);
        assertEquals(logItem.getFirmwareVersion(), receiver.getFirmwareVersion());
        assertEquals(logItem.getSerialNumber(), receiver.getManufacturerSerialNumber());
        assertEquals(logItem.getSatelliteSystem(), "GPS,Galileo");

        GnssReceiverType receiverB = mapper.mapToDto(logItem);
        assertEquals(receiverB.getFirmwareVersion(), logItem.getFirmwareVersion());
        assertEquals(receiverB.getManufacturerSerialNumber(), logItem.getSerialNumber());
        assertEquals(receiverB.getSatelliteSystem(), satelliteSystems);
    }
}

