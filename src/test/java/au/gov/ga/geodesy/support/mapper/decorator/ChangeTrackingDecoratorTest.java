package au.gov.ga.geodesy.support.mapper.decorator;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.ObjectFactory;
import net.opengis.gml.v_3_2_1.TimePositionType;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static au.gov.ga.geodesy.support.utils.GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_SEC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ChangeTrackingDecoratorTest {

    private static final int USE_THIS_HOUR = 22;
    private static final int USE_THIS_MINUTE = 13;
    private static final ObjectFactory geoObjectFactory = new ObjectFactory();

    @Test
    public void test01() throws ParseException {
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        assertThat(element.getDateInserted(), nullValue());
        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();

        GnssReceiverPropertyType out = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element, child);
        assertThat(out.getDateInserted(), notNullValue());

        GnssReceiverPropertyType out2 = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element, child);
        assertThat(out2.getDateInserted(), notNullValue());
    }

    @Test
    public void test01_1() throws ParseException {
        // Same as test01 but set a DateInserted
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        assertThat(element.getDateInserted(), nullValue());
        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();

        child.setDateInstalled(createTimePositionType());

        GnssReceiverPropertyType out = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element, child);
        assertThat(out.getDateInserted(), notNullValue());

        GnssReceiverPropertyType out2 = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element, child);
        assertThat(out2.getDateInserted(), notNullValue());
        assertThat(out2.getDateInserted().getValue().get(0), is(out.getDateInserted().getValue().get(0)));

        Instant theDate = GMLDateUtils.stringToDate(out2.getDateInserted().getValue().get(0), GEODESYML_DATE_FORMAT_TIME_SEC);

        assertThat(theDate.atOffset(ZoneOffset.UTC).getHour(), is(USE_THIS_HOUR));
        assertThat(theDate.atOffset(ZoneOffset.UTC).getMinute(), is(USE_THIS_MINUTE));
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
        assertThat(setDateInserted, nullValue());

        GnssReceiverType out = GeodesyMLDecorators.ChangeTrackingDecorator.addChangeTracking(element, element);
        assertThat(out, is(element));
    }

    // Now do the same but run them as part of their enclosing class - GeodesyMLDecorators

    @Test
    public void test01_2() {
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        assertThat(element.getDateInserted(), nullValue());

        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();

        GnssReceiverPropertyType out = GeodesyMLDecorators.addDecorators(element, child);
        assertThat(out.getDateInserted(), notNullValue());

        GnssReceiverPropertyType out2 = GeodesyMLDecorators.addDecorators(element, child);
        assertThat(out2.getDateInserted(), notNullValue());
    }

    @Test
    public void test01_2_2() {
        // Give the child a TimePositionType date
        GnssReceiverPropertyType element = new GnssReceiverPropertyType();
        assertThat(element.getDateInserted(), nullValue());

        // Child element of this - in theory - I don't need to connect them for this test
        GnssReceiverType child = geoObjectFactory.createGnssReceiverType();
        child.setDateInstalled(createTimePositionType());

        GnssReceiverPropertyType out = GeodesyMLDecorators.addDecorators(element, child);
        assertThat(out.getDateInserted(), notNullValue());

        GnssReceiverPropertyType out2 = GeodesyMLDecorators.addDecorators(element, child);
        assertThat(out2.getDateInserted(), notNullValue());
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
        assertThat(setDateInserted, nullValue());

        GnssReceiverType out = GeodesyMLDecorators.addDecorators(element, element);
        assertThat(out, is(element));
    }

    @Test
    public void test03_GNSSReceiver() {
        // After some changes the dateInstalled TimePositionType's value is not being mapped to the decorated object - find out why
        // Good test to have but this is not where the error is
        String testDate = "2007-12-03T10:15:30Z";
        GnssReceiverLogItem gnssReceiverLogItem = new GnssReceiverLogItem();
        gnssReceiverLogItem.setSerialNumber("1234");
        gnssReceiverLogItem.setDateInstalled(Instant.parse(testDate));
        GnssReceiverLogItem out = GeodesyMLDecorators.addDecorators(gnssReceiverLogItem);
        assertThat(out, notNullValue());
        assertThat(out.getDateInstalled(), notNullValue());
        assertThat(out.getDateInstalled().toString(), is(testDate));
    }

    private static TimePositionType createTimePositionType() {
        TimePositionType tpt = new TimePositionType();

        Instant dateTime = LocalDateTime.now().withHour(USE_THIS_HOUR).withMinute(USE_THIS_MINUTE).toInstant(ZoneOffset.UTC);

        tpt.setValue(Stream.of(GMLDateUtils.dateToString(dateTime, GEODESYML_DATE_FORMAT_TIME_SEC)).collect(Collectors.toList()));
        return tpt;
    }
}
