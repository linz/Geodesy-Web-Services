package au.gov.ga.geodesy.support.mapper.dozer.converter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    public static TimePrimitivePropertyType newOrUsingExistingTimePrimitivePropertyType(TimePrimitivePropertyType optionalExistingObject) {
        TimePrimitivePropertyType timePrimitivePropertyType = null;

        if (optionalExistingObject != null) {
            timePrimitivePropertyType = optionalExistingObject;
        } else {
            timePrimitivePropertyType = new TimePrimitivePropertyType();
        }
        return timePrimitivePropertyType;
    }

    /**
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
     * @param timePeriodType
     * @return JAXBElement genericised with given timePeriodType
     */
    static JAXBElement<TimePeriodType> buildJaxBElementTimePropertyType(TimePeriodType timePeriodType) {
        JAXBElement<TimePeriodType> jaxbTimePeriodType = getGmlObjectFactory().createTimePeriod(timePeriodType);
        return jaxbTimePeriodType;
    }

    /**
     * @param timePrimitivePropertyType
     * @return given timePrimitivePropertyType with a TimePeriodType payload
     */
    static TimePrimitivePropertyType addTimeInstantType(TimePrimitivePropertyType timePrimitivePropertyType) {
        TimeInstantType timePeriodType = getGmlObjectFactory().createTimeInstantType();
        JAXBElement<TimeInstantType> jaxbTimeInstantType = buildJaxBElementTimePropertyType(timePeriodType);

        timePrimitivePropertyType.setAbstractTimePrimitive(jaxbTimeInstantType);
        return timePrimitivePropertyType;
    }

    /**
     * @param timeInstantType
     * @return JAXBElement genericised with given timeInstantType
     */
    private static JAXBElement<TimeInstantType> buildJaxBElementTimePropertyType(TimeInstantType timeInstantType) {
        JAXBElement<TimeInstantType> jaxbTimeInstantType = getGmlObjectFactory().createTimeInstant(timeInstantType);
        return jaxbTimeInstantType;
    }

    /**
     * @param date
     * @return TimePositionType with the given date in text with GMLDateUtils.GEODESYML_DATE_FORMAT_TIME format.
     */
    public static TimePositionType buildTimePositionType(Instant date) {
        List<String> dateStrings = new ArrayList<>();
        if (date == null) {
            // Date wasn't included in data - leave List empty
        } else {
            dateStrings.add(GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC.format(date));
        }

        TimePositionType timePositionType = new TimePositionType();
        timePositionType.setValue(dateStrings);
        return timePositionType;
    }

    /**
     * DateFormat parse the given stringDate with all possible DateFormats and add a preferred text format to the returned TimePositionType
     *
     * @param stringDate
     * @return TimePositionType with the given date in text with GMLDateUtils DateFormat that is the output format for the DateFormat that
     * matches
     * @See au.gov.ga.geodesy.support.utils.GMLDateUtils
     */
    public static TimePositionType buildTimePositionType(String stringDate) {
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
     * <p>
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
     * <p>
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

    private static TimePrimitivePropertyType buildTimePrimitivePropertyType_TimeInstantType(Instant timePositionTypeDate) {
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils
                .addTimeInstantType(TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(null));
        TimePrimitivePropertyTypeUtils.getTheTimeInstantType(timePrimitivePropertyType)
                .setTimePosition(TimePrimitivePropertyTypeUtils.buildTimePositionType(timePositionTypeDate));

        return timePrimitivePropertyType;
    }

    /**
     * Build a TimePrimitivePropertyType containint a TimePeriodType.  If either of Being or End are null then the
     * beginPosition or endPosition of the TimePeriodDtype will be null.
     *
     * @param begin
     * @param end
     * @return
     */
    private static TimePrimitivePropertyType buildTimePrimitivePropertyType_TimePeriodType(Instant begin, Instant end) {
        TimePrimitivePropertyType timePrimitivePropertyType = TimePrimitivePropertyTypeUtils
                .addTimePeriodType(TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(null));
        TimePeriodType tpt = TimePrimitivePropertyTypeUtils.getTheTimePeriodType(timePrimitivePropertyType);
        if (begin != null) {
            tpt.setBeginPosition(TimePrimitivePropertyTypeUtils.buildTimePositionType(begin));
        }
        if (end != null) {
            tpt.setEndPosition(TimePrimitivePropertyTypeUtils.buildTimePositionType(end));
        }
        return timePrimitivePropertyType;
    }

    /**
     * @return new TimePrimitivePropertyType without any Time type payload
     */
    public static TimePrimitivePropertyType buildTimePrimitivePropertyType() {
        return TimePrimitivePropertyTypeUtils.newOrUsingExistingTimePrimitivePropertyType(null);
    }

    public static TimePrimitivePropertyType buildTimePrimitivePropertyType(Instant instant) {
        if (instant == null) {
            return buildTimePrimitivePropertyType();
        }
        return buildTimePrimitivePropertyType_TimeInstantType(instant);
    }

    /**
     * Build a TimePrimitivePropertyType containing a TimePeriodType.  If either of Being or End are null then the
     * beginPosition or endPosition of the TimePeriodDtype will be null.  If both are null the TimePrimitivePropertyType
     * will contain now Time payload.
     *
     * @param begin
     * @param end
     * @return new TimePrimitivePropertyType with optional TimePeriodType or TimeInstantType payload if those
     * arguments are given.
     */
    public static TimePrimitivePropertyType buildTimePrimitivePropertyType(Instant begin, Instant end) {
        if (begin == null && end == null) {
            return buildTimePrimitivePropertyType();
        }
        return buildTimePrimitivePropertyType_TimePeriodType(begin, end);
    }
}
