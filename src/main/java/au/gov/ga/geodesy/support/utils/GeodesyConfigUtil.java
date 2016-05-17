package au.gov.ga.geodesy.support.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

public class GeodesyConfigUtil {

    /**
     * 
     * @param clazz
     * @return the values of the "@PropertySource({"x","y"})" annotations (ie. x,y)
     */
    public static List<String> getPropertySource(Class<?> clazz) {
        PropertySource[] propertySources = clazz.getAnnotationsByType(PropertySource.class);
        List<String[]> x = Arrays.stream(propertySources).map(p -> p.value()).collect(Collectors.toList());
        List<String> propertyPaths = new ArrayList<>();
        // This should be possible such as with flatMap() and IntStream()
        for (String[] y : x) {
            propertyPaths.addAll(Arrays.stream(y).collect(Collectors.toList()));
        }
        return propertyPaths;
    }

    /**
     * 
     * @param clazz
     * @return the values of the "@PropertySource("classpath:x","classpath:y")" annotations (ie. x,y) and for each, drop any prefix before
     *         and including ':', which might be done for 'classpath:' on a PropertySource
     */
    public static List<String> getPropertySourceDropPrefix(Class<?> clazz) {
        List<String> propertySources = getPropertySource(clazz);
        return propertySources.stream().map(p -> p.replaceAll("[^:]*:", "")).collect(Collectors.toList());
    }

    /**
     * 
     * @param clazz
     * @return the values of the "@PropertySource("classpath:x","classpath:y")" annotations (ie. x,y) and for each, drop any prefix before
     *         and including ':', which might be done for 'classpath:' on a PropertySource. As a list of ClassResources.
     */
    public static ClassPathResource[] getPropertySourceDropPrefixAsClassResources(Class<?> clazz) {
        List<String> propertySources = getPropertySourceDropPrefix(clazz);

        ClassPathResource[] classPathResources = propertySources.stream().map(r -> new ClassPathResource(r))
                .collect(Collectors.toList()).toArray(new ClassPathResource[0]);
        return classPathResources;
    }
}
