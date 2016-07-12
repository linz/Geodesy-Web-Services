package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import net.opengis.gml.v_3_2_1.TimePositionType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class GeodesyMLDozerEventListener_GnssReceiverTypeTest {
    GnssReceiverTypePopulator listener = new GnssReceiverTypePopulator();

    @Test
    public void testNoDateRemoved() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();
        listener.checkElementPopulated(gnssReceiverType, "dateRemoved", getTimePositionType(null));
        assertThat(gnssReceiverType.getDateRemoved(), notNullValue());
    }

    @Test
    public void testNoDateRemoved2() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();
        String value = "a value";
        listener.checkElementPopulated(gnssReceiverType, "dateRemoved", getTimePositionType(value));
        assertThat(gnssReceiverType.getDateRemoved(), notNullValue());
        assertThat(gnssReceiverType.getDateRemoved().getValue().get(0), is(value));
    }

    @Test
    public void testYesDateRemoved() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();
        String value = "another value";
        gnssReceiverType.setDateRemoved(getTimePositionType(value));
        listener.checkElementPopulated(gnssReceiverType, "dateRemoved", getTimePositionType(null));
        assertThat(gnssReceiverType.getDateRemoved(), notNullValue());
        assertThat(gnssReceiverType.getDateRemoved().getValue().get(0), is(value));
    }

    private TimePositionType getTimePositionType(String value) {
        TimePositionType tpt = new TimePositionType();
        List<String> values = new ArrayList<String>();
        if (value != null) {
            values.add(value);
        }
        tpt.setValue(values);
        return tpt;
    }

}
