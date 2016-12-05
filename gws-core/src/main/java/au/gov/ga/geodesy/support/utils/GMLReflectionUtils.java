package au.gov.ga.geodesy.support.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GMLReflectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(GMLReflectionUtils.class);

    private static Map<String, Void> unwantedTypes = new HashMap<>();
    static {
        unwantedTypes.put("DOUBLE", null);
        unwantedTypes.put("BOOLEAN", null);
        unwantedTypes.put("STRING", null);
        unwantedTypes.put("INTEGER", null);
        unwantedTypes.put("CLASS", null);
        unwantedTypes.put("LIST", null);
    }

    /**
     * @param element
     * @return a List of getter method for the given element object that returns any getters that return non-primitive types. That is,
     *         not ones that return String, Double, double, boolean, Boolean, .... Used to recursively descend into the children of an
     *         element object
     */
    public static List<Method> getNonPrimitiveGetters(Object element) {
        Method[] methods = element.getClass().getMethods();
        List<Method> getters = Arrays.stream(methods)
                .filter(m -> m.getName().startsWith("get") && m.getParameterCount() == 0
                        && !unwantedTypes.containsKey(m.getReturnType().getSimpleName().toUpperCase()))
                .collect(Collectors.toList());
        logger.trace("  getNonPrimitiveGetters for: " + element.getClass().getSimpleName() + ": " + getters);
        return getters;
    }

    public static List<Method> getAllGetters(Object element) {
        Method[] methods = element.getClass().getMethods();
        List<Method> getters = Arrays.stream(methods)
                .filter(m -> m.getName().startsWith("get") && m.getParameterCount() == 0).collect(Collectors.toList());
        logger.trace("  getAllGetters for: " + element.getClass().getSimpleName() + ": " + getters);
        return getters;
    }

    /**
     * Given a setter name, return the equivalent getter name
     * 
     * @param setterName
     *            to change
     * @return getter form of the given setterName or "" if setterName isn't a setter
     */
    public static String changeGetterToSetter(String setterName) {
        if (setterName.startsWith("get")) {
            return "s" + setterName.substring(1);
        }
        return "";
    }
}
