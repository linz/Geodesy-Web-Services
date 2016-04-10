package au.gov.ga.geodesy.support.mapper.decorator;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.ObjectFactory;
import net.opengis.gml.v_3_2_1.TimePositionType;

public class ChangeTrackingDecoratorTest {

    private static final int USE_THIS_HOUR = 22;
    private static final int USE_THIS_MINUTE = 13;
    private static final ObjectFactory geoObjectFactory = new ObjectFactory();

    @Test
    public void test01() throws ParseException {
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        Assert.assertNull(element.getDateInserted());
        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();

        GnssReceiverPropertyType out = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element,child);
        Assert.assertNotNull(out.getDateInserted());

        GnssReceiverPropertyType out2 = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element,child);
        Assert.assertNotNull(out2.getDateInserted());
    }

    @Test
    public void test01_1() throws ParseException {
        // Same as test01 but set a DateInserted
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        Assert.assertNull(element.getDateInserted());
        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();

        child.setDateInstalled(createTimePositionType());

        GnssReceiverPropertyType out = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element,child);
        Assert.assertNotNull(out.getDateInserted());

        GnssReceiverPropertyType out2 = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element,child);
        Assert.assertNotNull(out2.getDateInserted());
        Assert.assertEquals(out.getDateInserted().getValue().get(0), out2.getDateInserted().getValue().get(0));

        Date theDate = GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_SEC.parse(out2.getDateInserted().getValue().get(0));
        Calendar theCal = GregorianCalendar.getInstance();
        theCal.setTime(theDate);
        Assert.assertEquals(USE_THIS_HOUR, theCal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(USE_THIS_MINUTE, theCal.get(Calendar.MINUTE));
    }

    @Test
    public void test02() {
        // Choose any POJO that doesn't have a setDateInserted
        GnssReceiverType element = new GnssReceiverType();
        Method setDateInserted = null;
        try {
            setDateInserted = element.getClass().getMethod("setDateInserted", String.class);
        } catch (NoSuchMethodException e) {
            // expected to come in here
        }
        Assert.assertNull(setDateInserted);

        GnssReceiverType out = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element, element);
        Assert.assertEquals(element, out);
    }

    // Now do the same but run them as part of their enclosing class - GeodesyMLDecorators

    @Test
    public void test01_2() {
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        Assert.assertNull(element.getDateInserted());

        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();

        GnssReceiverPropertyType out = GeodesyMLDecorators.addDecorators(element,child);
        Assert.assertNotNull(out.getDateInserted());

        GnssReceiverPropertyType out2 = GeodesyMLDecorators.addDecorators(element,child);
        Assert.assertNotNull(out2.getDateInserted());
    }
    
    @Test
    public void test01_2_2() {
        // Give the child a TimePositionType date
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        Assert.assertNull(element.getDateInserted());
        
        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();
        child.setDateInstalled(createTimePositionType());
        
        GnssReceiverPropertyType out = GeodesyMLDecorators.addDecorators(element,child);
        Assert.assertNotNull(out.getDateInserted());
        
        GnssReceiverPropertyType out2 = GeodesyMLDecorators.addDecorators(element,child);
        Assert.assertNotNull(out2.getDateInserted());
    }

    @Test
    public void test02_2() {
        // Choose any POJO that doesn't have a setDateInserted
        GnssReceiverType element = new GnssReceiverType();
        Method setDateInserted = null;
        try {
            setDateInserted = element.getClass().getMethod("setDateInserted", String.class);
        } catch (NoSuchMethodException e) {
            // expected to come in here
        }
        Assert.assertNull(setDateInserted);

        GnssReceiverType out = GeodesyMLDecorators.addDecorators(element,element);
        Assert.assertEquals(element, out);
    }

    private static TimePositionType createTimePositionType() {
        TimePositionType tpt = new TimePositionType();

        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, USE_THIS_HOUR);
        cal.set(Calendar.MINUTE, USE_THIS_MINUTE);
        tpt.setValue(
                Stream.of(GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_SEC.format(cal.getTime())).collect(Collectors.toList()));
        return tpt;
    }
}
