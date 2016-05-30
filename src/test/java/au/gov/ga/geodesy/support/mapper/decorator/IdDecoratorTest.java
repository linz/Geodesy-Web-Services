package au.gov.ga.geodesy.support.mapper.decorator;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.ga.geodesy.support.utils.GMLReflectionUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.ObjectFactory;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;

public class IdDecoratorTest {
    private ObjectFactory geoFactory = new ObjectFactory();
    private Instant dateTime = Instant.now();

    @Test
    public void test01_WithRecursiveIDApplication() {
        GeodesyMLType element = new GeodesyMLType();
        // Test recursively applying Ids with this child field element
        element.setValidTime(TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(dateTime));
        Assert.assertNotNull(element.getValidTime().getAbstractTimePrimitive().getValue());
        Assert.assertNull(element.getId());
        Assert.assertNull(element.getValidTime().getAbstractTimePrimitive().getValue().getId());

        GeodesyMLType out = GeodesyMLDecorators.IdDecorator.addId(element);
        Assert.assertNotNull(out.getId());
        Assert.assertNotNull(element.getValidTime().getAbstractTimePrimitive().getValue().getId());

        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        GeodesyMLType out2 = GeodesyMLDecorators.IdDecorator.addId(element);
        Assert.assertNotNull(out2.getId());
        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        Assert.assertEquals(out.getId(), out2.getId());
    }

    @Test
    public void test01_WithRecursiveIDApplication_NoIdOnParent() throws SecurityException {
        // I wrote the code and test01_WithRecursiveIDApplication and worked - but not in all cases and investigating.
        // This test is when run that code on parent that DOESN'T have id attribute but its child and descendents under that do.

        HumiditySensorPropertyType humidityParent = geoFactory.createHumiditySensorPropertyType();
        HumiditySensorType humidityChild = geoFactory.createHumiditySensorType();
        humidityParent.setHumiditySensor(humidityChild);
        humidityChild.setValidTime(TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(dateTime));
        Assert.assertNotNull(humidityChild.getValidTime().getAbstractTimePrimitive().getValue());
        // Confirm the parent hasn't an id attribute
        boolean foundExpectedException = false;
        try {
            humidityParent.getClass().getMethod("getId");
        } catch (NoSuchMethodException e) {
            foundExpectedException = true;
        }
        Assert.assertTrue("Expect there NOT to be an IdGetter method", foundExpectedException);
        Assert.assertNull(humidityChild.getId());
        Assert.assertNull(humidityChild.getValidTime().getAbstractTimePrimitive().getValue().getId());

        humidityParent = GeodesyMLDecorators.IdDecorator.addId(humidityParent);
        
        Assert.assertNotNull(humidityChild.getId());
        Assert.assertNotNull(humidityChild.getValidTime().getAbstractTimePrimitive().getValue().getId());
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

    @Test
    public void testRecursiveFunction01() {
        GMLDateUtils element = new GMLDateUtils();

        List<Method> getterMethods = GMLReflectionUtils.getNonPrimitiveGetters(element);
        Assert.assertEquals(0, getterMethods.size());
    }

    @Test
    public void testRecursiveFunction02() {
        GeodesyMLType element = new GeodesyMLType();

        List<Method> getterMethods = GMLReflectionUtils.getNonPrimitiveGetters(element);
        MatcherAssert.assertThat("getters #", getterMethods.size(), Matchers.greaterThan(4));
        System.out.println("Getters (#:" + getterMethods.size() + ") - " + getterMethods);
    }
}
