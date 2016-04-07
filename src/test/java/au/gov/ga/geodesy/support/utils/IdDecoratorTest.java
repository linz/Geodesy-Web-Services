package au.gov.ga.geodesy.support.utils;

import java.lang.reflect.Method;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;

public class IdDecoratorTest {

    @Test
    public void test01() {
        GeodesyMLType element = new GeodesyMLType();
        Assert.assertNull(element.getId());

        GeodesyMLType out = GeodesyMLDecorators.IdDecorator.addId(element);
        Assert.assertNotNull(out.getId());

        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        GeodesyMLType out2 = GeodesyMLDecorators.IdDecorator.addId(element);
        Assert.assertNotNull(out2.getId());
        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        Assert.assertEquals(out.getId(), out2.getId());
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

        GMLDateUtils out = GeodesyMLDecorators.IdDecorator.addId(gmlDateUtils);
        Assert.assertEquals(gmlDateUtils, out);
    }

    // Now do the same but run them as part of their enclosing class - GeodesyMLDecorators

    @Test
    public void test01_2() {
        GeodesyMLType element = new GeodesyMLType();
        Assert.assertNull(element.getId());

        GeodesyMLType out = GeodesyMLDecorators.addDecorators(element, element);
        Assert.assertNotNull(out.getId());
        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        GeodesyMLType out2 = GeodesyMLDecorators.addDecorators(element, element);
        Assert.assertNotNull(out2.getId());
        MatcherAssert.assertThat("the id", out2.getId(), Matchers.startsWith("GeodesyMLType_"));
        Assert.assertEquals(out.getId(), out2.getId());
    }

    @Test
    public void test02_2() {
        // Choose any POJO that doesn't have a setID
        GMLDateUtils gmlDateUtils = new GMLDateUtils();
        Method setId = null;
        try {
            setId = gmlDateUtils.getClass().getMethod("setId", String.class);
        } catch (NoSuchMethodException e) {
            // expected to come in here
        }
        Assert.assertNull(setId);

        GMLDateUtils out = GeodesyMLDecorators.addDecorators(gmlDateUtils, gmlDateUtils);
        Assert.assertEquals(gmlDateUtils, out);
    }
}
