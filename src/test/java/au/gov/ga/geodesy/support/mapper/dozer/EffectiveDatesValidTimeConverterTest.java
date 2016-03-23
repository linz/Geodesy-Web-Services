package au.gov.ga.geodesy.support.mapper.dozer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;
import au.gov.ga.geodesy.support.mapper.dozer.TimePrimitivePropertyTypeEffectiveDatesConverter;
import au.gov.ga.geodesy.support.mapper.dozer.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import net.opengis.gml.v_3_2_1.TimePositionType;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;

public class EffectiveDatesValidTimeConverterTest {

    private TimePrimitivePropertyTypeEffectiveDatesConverter edc = new TimePrimitivePropertyTypeEffectiveDatesConverter();
    private Date fromDate;
    private Date toDate;
    private static final boolean dontIncludeToOrEndDate = false;

    public EffectiveDatesValidTimeConverterTest() {
        Calendar fromCal = GregorianCalendar.getInstance(TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]));
        fromCal.add(Calendar.YEAR, -1);
        fromDate = fromCal.getTime();
        Calendar toCal = GregorianCalendar.getInstance(TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]));
        toDate = toCal.getTime();
    }

    @Test
    public void testEffectiveDatesSourceDestinationTimePeriodTypeNull() throws ParseException {
        TimePrimitivePropertyType destination = null;
        EffectiveDates source = getEffectiveDate();
        
        TimePrimitivePropertyType geodMlTimePrimitive = (TimePrimitivePropertyType) edc.convert(destination, source,
                TimePrimitivePropertyType.class, EffectiveDates.class);
        
        Assert.assertEquals(GMLDateUtils.dateToString(source.getFrom()), GMLDateUtils.stringToDateToString(
                getTPTBegin(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
        Assert.assertEquals(GMLDateUtils.dateToString(source.getTo()), GMLDateUtils.stringToDateToString(
                getTPTEnd(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
    }

    @Test
    public void testEffectiveDatesSourceDestinationTimePeriodTypeNotNull() throws ParseException {
        TimePrimitivePropertyType destination = new TimePrimitivePropertyType();
        EffectiveDates source = getEffectiveDate();

        TimePrimitivePropertyType geodMlTimePrimitive = (TimePrimitivePropertyType) edc.convert(destination, source,
                TimePrimitivePropertyType.class, EffectiveDates.class);

        Assert.assertEquals(GMLDateUtils.dateToString(source.getFrom()), GMLDateUtils.stringToDateToString(
                getTPTBegin(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
        Assert.assertEquals(GMLDateUtils.dateToString(source.getTo()), GMLDateUtils.stringToDateToString(
                getTPTEnd(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
    }

    @Test
    public void testTimePeriodTypeSourceDestinationEffectiveDatesNull() throws ParseException {
        EffectiveDates destination = null;

        TimePrimitivePropertyType source = getGoedMLEffectiveDates();
        EffectiveDates ed = (EffectiveDates) edc.convert(destination, source, EffectiveDates.class,
                TimePeriodType.class);

        Assert.assertEquals(GMLDateUtils.dateToString(fromDate), GMLDateUtils.dateToString(getEffectiveDateBegin(ed)));
        Assert.assertEquals(GMLDateUtils.dateToString(toDate), GMLDateUtils.dateToString(getEffectiveDateEnd(ed)));
    }

    @Test
    public void testTimePeriodTypeSourceDestinationEffectiveDatesNotNull() throws ParseException {
        EffectiveDates destination = getEffectiveDate();
        TimePrimitivePropertyType source = getGoedMLEffectiveDates();
        EffectiveDates ed = (EffectiveDates) edc.convert(destination, source, EffectiveDates.class,
                TimePeriodType.class);

        Assert.assertEquals(GMLDateUtils.dateToString(fromDate), GMLDateUtils.dateToString(getEffectiveDateBegin(ed)));
        Assert.assertEquals(GMLDateUtils.dateToString(toDate), GMLDateUtils.dateToString(getEffectiveDateEnd(ed)));
    }

    // // Run same again but with no to date

    @Test
    public void testEffectiveDatesSourceDestinationTimePeriodTypeNullNoToDate() throws ParseException {
        TimePrimitivePropertyType destination = null;
        EffectiveDates source = getEffectiveDate(dontIncludeToOrEndDate);
        
        TimePrimitivePropertyType geodMlTimePrimitive = (TimePrimitivePropertyType) edc.convert(destination, source,
                TimePrimitivePropertyType.class, EffectiveDates.class);

        Assert.assertEquals(GMLDateUtils.dateToString(source.getFrom()), GMLDateUtils.stringToDateToString(
                getTPTBegin(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
        Assert.assertEquals(GMLDateUtils.dateToString(source.getTo()), GMLDateUtils.stringToDateToString(
                getTPTEnd(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
    }

    @Test
    public void testEffectiveDatesSourceDestinationTimePeriodTypeNotNullNoToDate() throws ParseException {
        TimePrimitivePropertyType destination = new TimePrimitivePropertyType();
        EffectiveDates source = getEffectiveDate(dontIncludeToOrEndDate);

        TimePrimitivePropertyType geodMlTimePrimitive = (TimePrimitivePropertyType) edc.convert(destination, source,
                TimePrimitivePropertyType.class, EffectiveDates.class);

        Assert.assertEquals(GMLDateUtils.dateToString(source.getFrom()), GMLDateUtils.stringToDateToString(
                getTPTBegin(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
        Assert.assertEquals(GMLDateUtils.dateToString(source.getTo()), GMLDateUtils.stringToDateToString(
                getTPTEnd(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(geodMlTimePrimitive))));
    }

    @Test
    public void testTimePeriodTypeSourceDestinationEffectiveDatesNullNoToDate() throws ParseException {
        EffectiveDates destination = null;

        TimePrimitivePropertyType source = getGoedMLTimePrimitive(dontIncludeToOrEndDate);

        Assert.assertNull(TimePrimitivePropertyTypeUtils.getTheTimePeriodType(source).getEnd());

        EffectiveDates ed = (EffectiveDates) edc.convert(destination, source, EffectiveDates.class,
                TimePeriodType.class);

        Assert.assertEquals(GMLDateUtils.dateToString(fromDate), GMLDateUtils.dateToString(getEffectiveDateBegin(ed)));
        Assert.assertEquals(null, GMLDateUtils.dateToString(getEffectiveDateEnd(ed)));
    }

    @Test
    public void testTimePeriodTypeSourceDestinationEffectiveDatesNotNullNoToDate() throws ParseException {
        EffectiveDates destination = getEffectiveDate();
        TimePrimitivePropertyType source = getGoedMLTimePrimitive(dontIncludeToOrEndDate);
        EffectiveDates ed = (EffectiveDates) edc.convert(destination, source, EffectiveDates.class,
                TimePeriodType.class);

        Assert.assertEquals(GMLDateUtils.dateToString(fromDate), GMLDateUtils.dateToString(getEffectiveDateBegin(ed)));
        Assert.assertEquals(null, GMLDateUtils.dateToString(getEffectiveDateEnd(ed)));
    }

    /* ******** END TESTS, START UTILS ******** */

    private EffectiveDates getEffectiveDate() {
        return getEffectiveDate(true);
    }

    private EffectiveDates getEffectiveDate(boolean includeToDate) {
        EffectiveDates ed = new EffectiveDates();
        ed.setFrom(fromDate);
        if (includeToDate)
            ed.setTo(toDate);
        else
            ed.setTo(null);
        return ed;
    }

    private Date getEffectiveDateEnd(EffectiveDates ed) {
        return ed.getTo();
    }

    private Date getEffectiveDateBegin(EffectiveDates ed) {
        return ed.getFrom();
    }

    private TimePrimitivePropertyType getGoedMLEffectiveDates() {
        return getGoedMLTimePrimitive(true);
    }

    private TimePrimitivePropertyType getGoedMLTimePrimitive(boolean includeEndDate) {
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils
                .addTimePeriodType(TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(null));
        JAXBElement<TimePeriodType> jaxBTimePeriodType = TimePrimitivePropertyTypeUtils
                .buildJaxBElementTimePropertyType(getTimePeriodType(includeEndDate));
        timePrimitivePropertyType.setAbstractTimePrimitive(jaxBTimePeriodType);
        return timePrimitivePropertyType;
    }

    private TimePeriodType getTimePeriodType(boolean includeEndDate) {
        TimePeriodType tpt = new TimePeriodType();
        tpt.setBeginPosition(getBeginTPT());
        if (includeEndDate)
            tpt.setEndPosition(getEndTPT());
        else
            tpt.setEndPosition(null);

        return tpt;
    }

    /**
     * 
     * @return TimePositionType that contains the from / begin date
     */
    private TimePositionType getBeginTPT() {
        TimePositionType tpt = new TimePositionType();
        List<String> list = new ArrayList<>();
        list.add(GMLDateUtils.dateToString(fromDate));
        tpt.setValue(list);
        return tpt;
    }

    /**
     * @return TimePositionType that contains the to / end date
     */
    private TimePositionType getEndTPT() {
        TimePositionType tpt = new TimePositionType();
        List<String> list = new ArrayList<>();
        list.add(GMLDateUtils.dateToString(toDate));
        tpt.setValue(list);
        return tpt;
    }

    /**
     * @param tpt
     * @return beginning date from TimePeriodType
     */
    private String getTPTBegin(TimePeriodType tpt) {
        return tpt.getBeginPosition().getValue().get(0);
    }

    /**
     * @param tpt
     * @return end date from TimePeriodType or NULL if the end date does not exist
     */
    private String getTPTEnd(TimePeriodType tpt) {
        if (tpt.getEndPosition() == null || tpt.getEndPosition().getValue().size() == 0)
            return null;
        else
            return tpt.getEndPosition().getValue().get(0);
    }
}
