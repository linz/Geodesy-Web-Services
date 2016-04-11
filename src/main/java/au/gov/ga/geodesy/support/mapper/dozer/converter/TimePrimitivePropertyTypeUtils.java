package au.gov.ga.geodesy.support.mapper.dozer.converter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;

import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import net.opengis.gml.v_3_2_1.ObjectFactory;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import net.opengis.gml.v_3_2_1.TimePositionType;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;

public class TimePrimitivePropertyTypeUtils {
    private static ObjectFactory gmlObjectFactory;

    private static ObjectFactory getGmlObjectFactory() {
        if (gmlObjectFactory == null) {
            gmlObjectFactory = new ObjectFactory();
        }
        return gmlObjectFactory;
    }

    /**
     * Create a new or resurrect the optionalExistingObject (TimePrimitivePropertyType).
     * 
     * @param optionalExistingObject
     * @return new TimePrimitivePropertyType or resurrect the optionalExistingObject TimePrimitivePropertyType.
     */
    public static TimePrimitivePropertyType newOrUsingExistingTimePrimitivePropertyType(Object optionalExistingObject) {
        TimePrimitivePropertyType timePrimitivePropertyType = null;

        if (optionalExistingObject != null) {
            timePrimitivePropertyType = (TimePrimitivePropertyType) optionalExistingObject;
        } else {
            timePrimitivePropertyType = new TimePrimitivePropertyType();
        }
        return timePrimitivePropertyType;
    }

    /**
     * 
     * @param timePrimitivePropertyType
     * @return given timePrimitivePropertyType with a TimePeriodType payload
     */
    static TimePrimitivePropertyType addTimePeriodType(TimePrimitivePropertyType timePrimitivePropertyType) {
        TimePeriodType timePeriodType = getGmlObjectFactory().createTimePeriodType();
        JAXBElement<TimePeriodType> jaxbTimePeriodType = buildJaxBElementTimePropertyType(timePeriodType);

        timePrimitivePropertyType.setAbstractTimePrimitive(jaxbTimePeriodType);
        return timePrimitivePropertyType;
    }

    /**
     * 
     * @param timePeriodType
     * @return JAXBElement genericised with given timePeriodType
     */
    static JAXBElement<TimePeriodType> buildJaxBElementTimePropertyType(TimePeriodType timePeriodType) {
        JAXBElement<TimePeriodType> jaxbTimePeriodType = getGmlObjectFactory().createTimePeriod(timePeriodType);
        return jaxbTimePeriodType;
    }

    /**
     * 
     * @param timePrimitivePropertyType
     * @return given timePrimitivePropertyType with a TimePeriodType payload
     */
    public static TimePrimitivePropertyType addTimeInstantType(TimePrimitivePropertyType timePrimitivePropertyType) {
        TimeInstantType timePeriodType = getGmlObjectFactory().createTimeInstantType();
        JAXBElement<TimeInstantType> jaxbTimeInstantType = buildJaxBElementTimePropertyType(timePeriodType);

        timePrimitivePropertyType.setAbstractTimePrimitive(jaxbTimeInstantType);
        return timePrimitivePropertyType;
    }

    /**
     * 
     * @param timeInstantType
     * @return JAXBElement genericised with given timeInstantType
     */
    static JAXBElement<TimeInstantType> buildJaxBElementTimePropertyType(TimeInstantType timeInstantType) {
        JAXBElement<TimeInstantType> jaxbTimeInstantType = getGmlObjectFactory().createTimeInstant(timeInstantType);
        return jaxbTimeInstantType;
    }

    /**
     * 
     * @param date
     * @return TimePositionType with the given date in text with GMLDateUtils.GEODESYML_DATE_FORMAT_TIME format.
     */
    public static TimePositionType buildTimePositionType(Date date) {
        List<String> dateStrings = new ArrayList<>();
        if (date == null) {
            // Date wasn't included in data - leave List empty
        } else {
            dateStrings.add(GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_SEC.format(date));
        }

        TimePositionType timePositionType = new TimePositionType();
        timePositionType.setValue(dateStrings);
        return timePositionType;
    }

    /**
     * DateFormat parse the given stringDate with all possible DateFormats and add a preferred text format to the returned TimePositionType
     * 
     * @See au.gov.ga.geodesy.support.utils.GMLDateUtils
     * @param stringDate
     * @return TimePositionType with the given date in text with GMLDateUtils DateFormat that is the output format for the DateFormat that
     *         matches
     */
    static TimePositionType buildTimePositionType(String stringDate) {
        List<String> dateStrings = new ArrayList<>();
        if (stringDate == null) {
            // Date wasn't included in data - leave List empty
        } else {
            dateStrings.add(GMLDateUtils.stringToDateToStringMultiParsers(stringDate));
        }

        TimePositionType timePositionType = new TimePositionType();
        timePositionType.setValue(dateStrings);
        return timePositionType;
    }

    /**
     * TimePrimitivePropertyType can hold various payloads. It will be a ClassCastException if the wrong type is asked for so be aware of
     * which type is used where.
     * 
     * Also @see TimePeriodPropertyTypeUtils#getTheTimeInstantType(TimePrimitivePropertyType)
     * 
     * @param timePrimitivePropertyType
     * @return TimePeriodType payload of the given timePrimitivePropertyType
     */
    public static TimePeriodType getTheTimePeriodType(TimePrimitivePropertyType timePrimitivePropertyType) {
        TimePeriodType timePeriodType = (TimePeriodType) timePrimitivePropertyType.getAbstractTimePrimitive()
                .getValue();
        return timePeriodType;
    }

    /**
     * TimePrimitivePropertyType can hold various payloads. It will be a ClassCastException if the wrong type is asked for so be aware of
     * which type is used where.
     * 
     * Also @see {@link #getTheTimePeriodType(TimePrimitivePropertyType)}
     * 
     * @param timePrimitivePropertyType
     * @return TimeInstantType payload of the given timePrimitivePropertyType
     */
    public static TimeInstantType getTheTimeInstantType(TimePrimitivePropertyType timePrimitivePropertyType) {
        TimeInstantType timeInstantType = (TimeInstantType) timePrimitivePropertyType.getAbstractTimePrimitive()
                .getValue();
        return timeInstantType;
    }
    
    public static TimePrimitivePropertyType buildTimePrimitivePropertyType(Date timePositionTypeDate) {
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils
                .addTimeInstantType(TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(null));
        TimePrimitivePropertyTypeUtils.getTheTimeInstantType(timePrimitivePropertyType)
                .setTimePosition(TimePrimitivePropertyTypeUtils.buildTimePositionType(timePositionTypeDate));

        return timePrimitivePropertyType;
    }

    public static Date buildStartOfTime() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+10"));
        cal.set(Calendar.YEAR,1970);
        cal.set(Calendar.MONTH,Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH,23);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return cal.getTime();
    }

}
