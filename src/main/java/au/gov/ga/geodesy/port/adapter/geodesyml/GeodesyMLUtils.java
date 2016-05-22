package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.xml.icsm.geodesyml.v_0_3.ObjectFactory;

public class GeodesyMLUtils {
    private static Logger logger = LoggerFactory.getLogger(GeodesyMLUtils.class);

    @SuppressWarnings("unchecked")
    /**
     * Our JAXB unmarshalling (due to the schema) creates List<JAXBElement<?>> elements and this can be used to find specific elements from
     * this List
     * 
     * @param elements
     *            to find our type in
     * @param type
     *            to find
     * @return the element from the list of the given type
     */
    public static <T> Stream<T> getElementFromJAXBElements(List<JAXBElement<?>> elements, Class<T> type) {
        // List<JAXBElement<?>> elements = geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance();
        return elements.stream()
                .map(JAXBElement::getValue)
                .filter(x -> type.isAssignableFrom(x.getClass()))
                .map(x -> (T) x);
    }

    /**
     * Create a JAXBElement<?> equivalent of the given element. Used because we have lots of these generic elements in use, such as in
     * GeodesyMLtype.nodeOrAbstractPositionOrPositionPairCovariance, which is a List<JAXBElement<?>>.
     * 
     * @param geodesyMLElement
     *            - GeodesyML Element to build JAXBElement for
     * @param JAXBElementType
     *            ?????
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> JAXBElement<T> buildJAXBElementEquivalent(Object geodesyMLElement)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ObjectFactory geodesyObjectFactory = new ObjectFactory();

        ArrayList<String> possibleCreateMethodNames = buildPossibleCreateMethodNames(geodesyMLElement.getClass());
        logger.trace("buildJAXBElementEquivalent: " + stringifyList(possibleCreateMethodNames));

        boolean foundMethod = true;
        Method equivalentMethod = null;

        for (String possibleMethodName : possibleCreateMethodNames) {
            try {
                equivalentMethod = geodesyObjectFactory.getClass().getMethod(possibleMethodName,
                        geodesyMLElement.getClass());
                // Check the return type is JAXBElement
                if (equivalentMethod.getReturnType().equals(JAXBElement.class)) {
                    foundMethod = true;
                }
                break;
            } catch (NoSuchMethodException | SecurityException e) {
                logger.debug(String.format("Method '%s' DOESN'T exist on '%s'", possibleMethodName,
                        geodesyObjectFactory.getClass().getName()));
                foundMethod = false;
            }
        }
        if (!foundMethod) {
            throw new GeodesyRuntimeException(String.format("ERROR: No methods found on class %s with these names: %s",
                    geodesyObjectFactory.getClass().getName(), stringifyList(possibleCreateMethodNames)));
        }
        logger.debug(String.format("Method '%s' DOES exist on '%s' ", equivalentMethod.getName(),
                geodesyObjectFactory.getClass().getName()));

        return (JAXBElement<T>) equivalentMethod.invoke(geodesyObjectFactory, geodesyMLElement);
    }

    /**
     * Reflection is used to create the element. This returns the possible method names in the ObjectFactory - 'create'X where X is the
     * class name of the source object but capitalised and one option is without any trailing 'Type' and another is with.
     * 
     * @param sourceClass
     *            of type to create element for
     * @return List of possible method names on the ObjectFactory that creates the element
     */
    private static ArrayList<String> buildPossibleCreateMethodNames(Class<? extends Object> sourceClass) {
        String pre = "create";
        String upper = upperCaseFirstCharacter(sourceClass.getSimpleName());
        String withTypePostfix = upper;
        String woutTypePostfix = upper.replace("Type", "");

        Set<String> possibleMethodNames = new HashSet<>();

        possibleMethodNames.add(pre + woutTypePostfix);
        possibleMethodNames.add(pre + withTypePostfix);

        return new ArrayList<String>(possibleMethodNames);
    }

    private static String upperCaseFirstCharacter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String stringifyList(List<String> stringList) {
        return stringList.stream().collect(Collectors.joining(",", "(", ")"));
    }

    /**
     * Wrap argument in JAXBElement.
     */
    // TODO: robustness, what about buildJAXBElementEquivalent?
    @SuppressWarnings("unchecked")
    public static <T> JAXBElement<T> wrapInJAXBElement(T t) {
        String typeName = t.getClass().getSimpleName();
        String factoryName = t.getClass().getPackage().getName() + ".ObjectFactory";
        String factoryMethodName = "create" + typeName.substring(0, typeName.length() - "Type".length());
        try {
            Class<?> factoryClass = Class.forName(factoryName);
            Object factory = factoryClass.newInstance();
            Method factoryMethod;
            factoryMethod = factoryClass.getMethod(factoryMethodName, new Class<?>[] { t.getClass() });
            JAXBElement<T> element;
            element = (JAXBElement<T>) factoryMethod.invoke(factory, new Object[] { t });
            return element;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
