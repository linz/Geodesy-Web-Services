package au.gov.ga.geodesy.support.mapper.dozer;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import org.testng.annotations.Test;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class DozerDelegateTest {
    @Test
    public void mapWithGuardWithDecorators() throws Exception {
        // After some changes the dateInstalled TimePositionType's value is not being mapped to the decorated object - find out why
        // The problem was the translator mapping (ie. just the XML) - it needed to change from Date to Instant
        String testDate = "2007-12-03T10:15:31Z";
        GnssReceiverLogItem gnssReceiverLogItem = new GnssReceiverLogItem();
        gnssReceiverLogItem.setSerialNumber("1234");
        gnssReceiverLogItem.setDateInstalled(Instant.parse(testDate));
        GnssReceiverType out = DozerDelegate.mapWithGuardWithDecorators(gnssReceiverLogItem, GnssReceiverType.class);
        assertThat(out, notNullValue());
        assertThat(out.getDateInstalled(), notNullValue());
        assertThat(out.getDateInstalled().getValue().get(0), is(testDate));
    }

}