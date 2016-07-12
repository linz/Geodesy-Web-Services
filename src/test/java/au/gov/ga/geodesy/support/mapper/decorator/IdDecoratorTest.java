package au.gov.ga.geodesy.support.mapper.decorator;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.ga.geodesy.support.utils.GMLReflectionUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.ObjectFactory;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

public class IdDecoratorTest {
    private ObjectFactory geoFactory = new ObjectFactory();
    private Instant dateTime = Instant.now();

    @Test
    public void test01_WithRecursiveIDApplication() {
        GeodesyMLType element = new GeodesyMLType();
        // Test recursively applying Ids with this child field element
        element.setValidTime(TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(dateTime));
        assertThat(element.getValidTime().getAbstractTimePrimitive().getValue(), notNullValue());
        assertThat(element.getId(), nullValue());

        assertThat(element.getValidTime().getAbstractTimePrimitive().getValue().getId(), nullValue());

        GeodesyMLType out = GeodesyMLDecorators.IdDecorator.addId(element);
        assertThat(out.getId(), notNullValue());
        assertThat(element.getValidTime().getAbstractTimePrimitive().getValue().getId(), notNullValue());

        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        GeodesyMLType out2 = GeodesyMLDecorators.IdDecorator.addId(element);
        assertThat(out2.getId(), notNullValue());
        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        assertThat(out2.getId(), is(out.getId()));
    }

    @Test
    public void test01_WithRecursiveIDApplication_NoIdOnParent() throws SecurityException {
        // I wrote the code and test01_WithRecursiveIDApplication and worked - but not in all cases and investigating.
        // This test is when run that code on parent that DOESN'T have id attribute but its child and descendents under that do.

        HumiditySensorPropertyType humidityParent = geoFactory.createHumiditySensorPropertyType();
        HumiditySensorType humidityChild = geoFactory.createHumiditySensorType();
        humidityParent.setHumiditySensor(humidityChild);
        humidityChild.setValidTime(TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(dateTime));
        assertThat(humidityChild.getValidTime().getAbstractTimePrimitive().getValue(), notNullValue());
        // Confirm the parent hasn't an id attribute
        boolean foundExpectedException = false;
        try {
            humidityParent.getClass().getMethod("getId");
        } catch (NoSuchMethodException e) {
            foundExpectedException = true;
        }
        assertThat("Expect there NOT to be an IdGetter method", foundExpectedException, is(true));
        assertThat(humidityChild.getId(), nullValue());
        assertThat(humidityChild.getValidTime().getAbstractTimePrimitive().getValue().getId(), nullValue());

        humidityParent = GeodesyMLDecorators.IdDecorator.addId(humidityParent);
        
        assertThat(humidityChild.getId(), notNullValue());
        assertThat(humidityChild.getValidTime().getAbstractTimePrimitive().getValue().getId(), notNullValue());
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
        assertThat(setId, nullValue());

        GMLDateUtils out = GeodesyMLDecorators.IdDecorator.addId(gmlDateUtils);
        assertThat(out, is(gmlDateUtils));
    }

    // Now do the same but run them as part of their enclosing class - GeodesyMLDecorators

    @Test
    public void test01_2() {
        GeodesyMLType element = new GeodesyMLType();
        assertThat(element.getId(), nullValue());

        GeodesyMLType out = GeodesyMLDecorators.addDecorators(element, element);
        assertThat(out.getId(), notNullValue());
        MatcherAssert.assertThat("the id", out.getId(), Matchers.startsWith("GeodesyMLType_"));

        GeodesyMLType out2 = GeodesyMLDecorators.addDecorators(element, element);
        assertThat(out2.getId(), notNullValue());
        MatcherAssert.assertThat("the id", out2.getId(), Matchers.startsWith("GeodesyMLType_"));
        assertThat(out2.getId(), is(out.getId()));
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
        assertThat(setId, nullValue());

        GMLDateUtils out = GeodesyMLDecorators.addDecorators(gmlDateUtils, gmlDateUtils);
        assertThat(out, is(gmlDateUtils));
    }

    @Test
    public void testRecursiveFunction01() {
        GMLDateUtils element = new GMLDateUtils();

        List<Method> getterMethods = GMLReflectionUtils.getNonPrimitiveGetters(element);
        assertThat(getterMethods.size(), is(0));
    }

    @Test
    public void testRecursiveFunction02() {
        GeodesyMLType element = new GeodesyMLType();

        List<Method> getterMethods = GMLReflectionUtils.getNonPrimitiveGetters(element);
        MatcherAssert.assertThat("getters #", getterMethods.size(), Matchers.greaterThan(4));
        System.out.println("Getters (#:" + getterMethods.size() + ") - " + getterMethods);
    }
}
