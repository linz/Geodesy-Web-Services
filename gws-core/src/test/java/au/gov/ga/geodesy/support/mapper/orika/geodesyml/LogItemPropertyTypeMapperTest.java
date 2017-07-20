package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.support.spring.UnitTest;
import au.gov.ga.geodesy.support.utils.DateTimeFormatDecorator;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_4.GnssReceiverPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_4.GnssReceiverType;

import net.opengis.gml.v_3_2_1.TimePositionType;

public class LogItemPropertyTypeMapperTest extends UnitTest {

    private LogItemPropertyTypeMapper<GnssReceiverPropertyType, GnssReceiverType, GnssReceiverLogItem> mapper;

    @Autowired
    public void setGnssReceiverMapper(GnssReceiverMapper gnssReceiverMapper) {
        mapper = new LogItemPropertyTypeMapper<>(gnssReceiverMapper);
    }

    @Test
    public void mapFromDto() {
        Instant dateInserted = Instant.now().minusMillis(2000);
        Instant dateDeleted = Instant.now().minusMillis(1000);
        String deletedReason = "deleted reason";
        String serialNumber = "1";

        // DTO to domain
        GnssReceiverPropertyType receiverProperty = new GnssReceiverPropertyType()
            .withGnssReceiver(
                new GnssReceiverType()
                    .withManufacturerSerialNumber(serialNumber)
            )
            .withDateInserted(timePosition(dateInserted))
            .withDateDeleted(timePosition(dateDeleted))
            .withDeletedReason(deletedReason)
        ;

        GnssReceiverLogItem receiver = mapper.to(receiverProperty);
        assertThat(receiver.getSerialNumber(), is(equalTo(serialNumber)));

        assertThat(receiver.getDateInserted(), is(equalTo(dateInserted)));
        assertThat(receiver.getDateDeleted(), is(equalTo(dateDeleted)));
        assertThat(receiver.getDeletedReason(), is(equalTo(deletedReason)));

        // domain to DTO
        GnssReceiverPropertyType receiverPropertyB = mapper.from(receiver);
        assertThat(receiverPropertyB.getGnssReceiver().getManufacturerSerialNumber(), is(equalTo(serialNumber)));

        assertThat(receiverPropertyB.getDateInserted(), is(equalTo(timePosition(dateInserted))));
        assertThat(receiverPropertyB.getDateDeleted(), is(equalTo(timePosition(dateDeleted))));
        assertThat(receiverPropertyB.getDeletedReason(), is(equalTo(deletedReason)));
    }

    private TimePositionType timePosition(Instant date) {
        TimePositionType timePosition = new TimePositionType();
        timePosition.getValue().add(GMLDateUtils.dateToString(date, DateTimeFormatDecorator.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX")));
        return timePosition;
    }
}
