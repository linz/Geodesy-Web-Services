package au.gov.ga.geodesy.support.utils;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;

public class IdStamperTest {

    @Test
    public void test01() {
        GeodesyMLType element = new GeodesyMLType();
        Assert.assertNull(element.getId());

        GeodesyMLType out = IdStamper.addId(element);
        Assert.assertNotNull(out.getId());
        Assert.assertEquals("GeodesyMLType_0", out.getId());

        GeodesyMLType out2 = IdStamper.addId(element);
        Assert.assertNotNull(out2.getId());
        Assert.assertEquals("GeodesyMLType_1", out2.getId());
    }

    @Test
    public void test02() {
        // Choose any POJO that doesn't have a setID
        GMLDateUtils gmlDateUtils = new GMLDateUtils();
        Method setId = null;
        try {
            setId = gmlDateUtils.getClass().getMethod("setId", String.class);
        } catch (NoSuchMethodException e) {
            // expected to come in here
        }
        Assert.assertNull(setId);
        
        GMLDateUtils out = IdStamper.addId(gmlDateUtils);
        Assert.assertEquals(gmlDateUtils, out);
    }
}
