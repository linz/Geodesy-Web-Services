package au.gov.ga.geodesy.support.mapper.dozer.populator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.support.utils.GMLReflectionUtils;

/**
 * Abstract class for populating some element (the parent element) with child elements by Setting them if they currently have no value.
 * 
 * It is wired in as a DozerEventListner (http://dozer.sourceforge.net/documentation/events.html).
 * 
 * @author brookes
 *
 */
public abstract class GeodesyMLElementPopulator<T> implements DozerEventListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * DozerEventListener method that is not needed.
     */
    @Override
    public void mappingStarted(DozerEvent event) {
    }

    /**
     * DozerEventListener method that is not needed.
     */
    @Override
    public void preWritingDestinationValue(DozerEvent event) {
    }

    /**
     * DozerEventListener method that is not needed.
     */
    @Override
    public void mappingFinished(DozerEvent event) {
        @SuppressWarnings("unchecked")
        T parentObject = (T) event.getDestinationObject();
        checkAllRequiredElementsPopulated(parentObject);
    }

    /**
     * DozerEventListener method that we use to check and populate the element with children by setting fields on it.
     */
    @Override
    public void postWritingDestinationValue(DozerEvent event) {
    }

    /**
     * The implementation of this is used to check if specific child elements have a value and if not then to set them. This is done using
     * the helper method {@link #checkElementPopulated(Object, String, Object)}.
     * 
     * @param parentObject
     *            that has the child elements under consideration
     */
    abstract void checkAllRequiredElementsPopulated(T parentObject);

    /**
     * This is to check the specific child elements and set if no value.
     * 
     * @param parentObject
     *            that has the child element under consideration
     * @param elementName
     *            is the name of the child elment (field) belonging to the parentObject
     * @param defaultObj
     *            is the object to set for the child element
     */
    void checkElementPopulated(T parentObject, String elementName, Object defaultObj) {
        create(parentObject, elementName, defaultObj);
    }

    void create(Object parentObject, String elementName, Object defaultObj) {
        try {
            Method methodGet = parentObject.getClass().getMethod("get" + capitalise(elementName));
            Object foundObject = methodGet.invoke(parentObject);
            if (foundObject == null) {
                createWithSetter(parentObject, elementName, defaultObj);
            } else {
                checkSubElementsPopulated(foundObject, defaultObj);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new GeodesyRuntimeException(
                    String.format("Expecting method to exist for type: %s (or perhaps other reflection error)",
                            parentObject.getClass()),
                    e);
        }
    }

    /**
     * Create the field of given parentObject with the given defaultObject though setter for field elementName
     * 
     * @param parentObject
     * @param elementName
     * @param defaultObj
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private void createWithSetter(Object parentObject, String elementName, Object defaultObj)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method methodSet = parentObject.getClass().getMethod("set" + capitalise(elementName), defaultObj.getClass());
        logger.warn(String.format("Possible data error - needed to add required element \"%s\" in this parent: %s",
                elementName, parentObject));
        methodSet.invoke(parentObject, defaultObj);
    }

    /**
     * The parentObject has an instance of defaultObj so can't set that. However in the case that the object isn't simple and has sub-fields
     * that are null in the given foundObject they need to be set from the given defaultObj
     * 
     * @param parentObject
     *            - the parentObject that has the foundObject
     * @param foundObject
     *            - that is a field of the parentObject
     * @param defaultObj
     *            - the default object to be set in-place of the foundObject, but since the foundObject exists, set the fields of
     *            foundObject from it
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private void checkSubElementsPopulated(Object foundObject, Object defaultObj) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (defaultObj.getClass() == foundObject.getClass()) {
            List<Method> getMethods = GMLReflectionUtils.getAllGetters(foundObject);
            for (Method m : getMethods) {
                Object foundObjectsField = m.invoke(foundObject);
                Object defaultObjectsField = m.invoke(defaultObj);
                if (foundObjectsField == null && defaultObjectsField != null) {
                    String setterName = GMLReflectionUtils.changeGetterToSetter(m.getName());
                    Method setter = foundObject.getClass().getMethod(setterName, m.getReturnType());
                    setter.invoke(foundObject, defaultObjectsField);
                }
            }
        }
    }

    private static String capitalise(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }
}
