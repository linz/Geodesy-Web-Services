package au.gov.ga.geodesy.support.mapper.dozer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dozer.DozerBeanMapper;

import au.gov.ga.geodesy.support.mapper.dozer.populator.GeodesyMLDozerEventListenerFactory;
import au.gov.ga.geodesy.support.utils.GeodesyMLDecorators;

/**
 * The main purpose of this is to delegate calls to the Dozer mapper to catch and manage nulls.
 *
 */
public class DozerDelegate {
    private static DozerBeanMapper mapper;

    private DozerDelegate() {
    }

    private static DozerBeanMapper getMapper() {
        if (mapper == null) {
            mapper = new DozerBeanMapper();
            List<String> dozerMappings = new ArrayList<>();
            dozerMappings.add("dozer/ConverterMappings.xml");
            dozerMappings.add("dozer/FieldMappings.xml");
            mapper.setMappingFiles(dozerMappings);
            mapper.setEventListeners(Stream.of(new GeodesyMLDozerEventListenerFactory()).collect(Collectors.toList()));
        }
        return mapper;
    }

    /**
     * Perform the Dozer mapping call but first check the sourceObject isn't null
     * 
     * @param sourceObject
     *            to map
     * @param destinationClass
     *            the type of object to map to
     * @return the sourceObject mapped to object of type destinationClass, or null if sourceObject is null.
     */
    public static <T> T mapWithGuard(Object sourceObject, Class<T> destinationClass) {
        if (sourceObject != null) {
            T mapped = getMapper().map(sourceObject, destinationClass);
            return mapped;
        } else {
            return null;
        }
    }

    /**
     * Perform the Dozer mapping call but first check the sourceObject isn't null. Apply the decorators to the mapped object.
     * 
     * @param sourceObject
     *            to map
     * @param destinationClass
     *            the type of object to map to
     * @return the sourceObject mapped to object of type destinationClass with decorators applied, or null if sourceObject is null.
     */
    public static <T> T mapWithGuardWithDecorators(Object sourceObject, Class<T> destinationClass) {
        if (sourceObject != null) {
            T mappedWithDecorators = GeodesyMLDecorators.addDecorators(getMapper().map(sourceObject, destinationClass));
            return mappedWithDecorators;
        } else {
            return null;
        }
    }
}
