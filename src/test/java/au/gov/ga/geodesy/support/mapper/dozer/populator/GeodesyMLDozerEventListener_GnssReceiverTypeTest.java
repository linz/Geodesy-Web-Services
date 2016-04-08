package au.gov.ga.geodesy.support.mapper.dozer.populator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.testng.Assert;

import au.gov.ga.geodesy.support.mapper.dozer.populator.GeodesyMLDozerEventListener_GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import net.opengis.gml.v_3_2_1.TimePositionType;

public class GeodesyMLDozerEventListener_GnssReceiverTypeTest {
    GeodesyMLDozerEventListener_GnssReceiverType listener = new GeodesyMLDozerEventListener_GnssReceiverType();
    
    @Test
    public void testNoDateRemoved() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();
        listener.checkElementPopulated(gnssReceiverType, "dateRemoved", getTimePositionType(null));
        Assert.assertNotNull(gnssReceiverType.getDateRemoved());
    }
    @Test
    public void testNoDateRemoved2() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();
        String value = "a value";
        listener.checkElementPopulated(gnssReceiverType, "dateRemoved", getTimePositionType(value));
        Assert.assertNotNull(gnssReceiverType.getDateRemoved());
        Assert.assertEquals(value,  gnssReceiverType.getDateRemoved().getValue().get(0));
    }
    @Test
    public void testYesDateRemoved() {
        GnssReceiverType gnssReceiverType = new GnssReceiverType();
        String value = "another value";
        gnssReceiverType.setDateRemoved(getTimePositionType(value));
        listener.checkElementPopulated(gnssReceiverType, "dateRemoved", getTimePositionType(null));
        Assert.assertNotNull(gnssReceiverType.getDateRemoved());
        Assert.assertEquals(value,  gnssReceiverType.getDateRemoved().getValue().get(0));
    }

    private TimePositionType getTimePositionType(String value) {
        TimePositionType tpt = new TimePositionType();
        List<String> values =  new ArrayList<String>();
        if (value != null) {
            values.add(value);
        }
        tpt.setValue(values);
        return tpt;
    }

}
