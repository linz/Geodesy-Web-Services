package au.gov.ga.geodesy.support.mapper.dozer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * The receivers have required elements that don't all exist in the SOPAC Sitelog xml. This fills them in.
 * Note that it isn't possible to do every element as they will have complex element hierarchies.
 * 
 * @author brookes
 *
 */
public class GeodesyMLDozerEventListener_GnssReceiverType implements DozerEventListener {
    Logger logger = LoggerFactory.getLogger(getClass());
    Class<?> type = GnssReceiverType.class;

    @Override
    public void mappingStarted(DozerEvent event) {
    }

    @Override
    public void preWritingDestinationValue(DozerEvent event) {
    }

    @Override
    public void postWritingDestinationValue(DozerEvent event) {
        GnssReceiverType gnssReceiverType = (GnssReceiverType) event.getDestinationObject();
        checkPopulateAllRequiredElements(gnssReceiverType);
    }

    @Override
    public void mappingFinished(DozerEvent event) {
    }

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    private void checkPopulateAllRequiredElements(GnssReceiverType gnssReceiverType) {
        // This can be blank when receiver hasn't been removed. Some other logic in the project
        // removes empty elements from the Sopac SiteLog before it gets to this translator
        checkPopulateElement(gnssReceiverType, "dateRemoved", getBlankTimePositionType());
    }

    private TimePositionType getBlankTimePositionType() {
        TimePositionType tpt = new TimePositionType();
        tpt.setValue((List<String>) new ArrayList<String>());
        return tpt;
    }

    void checkPopulateElement(GnssReceiverType gnssReceiverType, String elementName, Object defaultObj) {
        try {
            // Despite being for a specific type, I used reflection to be able to easily move this to being
            // a generic solution
            Method methodGet = type.getMethod("get" + capitalise(elementName));
            Object x = methodGet.invoke(gnssReceiverType);
            if (x == null) {
                Method methodSet = type.getMethod("set" + capitalise(elementName), defaultObj.getClass());
                methodSet.invoke(gnssReceiverType, defaultObj);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            logger.error(
                    String.format("Expecting method to exist for type: %s (or perhaps other reflection error)", type),
                    e);
        }
    }

    private String capitalise(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }
}
