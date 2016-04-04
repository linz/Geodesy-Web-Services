package au.gov.ga.geodesy.support.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;

public class IdStamper {

    private static Map<Class<?>, Integer> uniqueIdMap = new HashMap<>();

    /**
     * Add unique id per element class type to the element and return.
     * 
     * @param element
     *            - that should have an Id attribute (but will check if so)
     * @param T
     *            - element class
     * @return Same element but with id attribute set to unique id (if it exists)
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> T addId(T element) {
        try {
            return addIdRunner(element);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new GeodesyRuntimeException("Error generating id for element: " + element.getClass(), e);
        }
    }

    private static <T> T addIdRunner(T element)
            throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method setString = null, setInteger = null;

        try {
            setString = element.getClass().getMethod("setId", String.class);
        } catch (NoSuchMethodException e) {
            // No problem - maybe its setId(Integer)
            try {
                setInteger = element.getClass().getMethod("setId", Integer.class);
            } catch (NoSuchMethodException e2) {
                // No problem - mustn't have a setId(Integer or String)
                return element;
            }
        }
        if (setString != null) {
            String id = getStringId(element.getClass());
            setString.invoke(element, element.getClass().getSimpleName()+"_"+id);
        } else if (setInteger != null) {
            Integer id = getIntegerId(element.getClass());
            setInteger.invoke(element, id);
        }
        return element;
    }

    private static Integer getIntegerId(Class<? extends Object> elementClass) {
        Integer id = null;
        if (uniqueIdMap.containsKey(elementClass)) {
            id = uniqueIdMap.get(elementClass);
        } else {
            id = -1;
        }
        uniqueIdMap.put(elementClass, ++id);
        return id;
    }

    private static String getStringId(Class<? extends Object> elementClass) {
        return getIntegerId(elementClass).toString();
    }

}
