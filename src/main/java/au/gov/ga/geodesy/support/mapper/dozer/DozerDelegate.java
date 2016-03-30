package au.gov.ga.geodesy.support.mapper.dozer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dozer.DozerBeanMapper;

/**
 * The main purpose of this is to delegate calls to the Dozer mapper to catch and manage nulls.
 *
 */
public class DozerDelegate {
    private static DozerBeanMapper mapper;

    private DozerDelegate() {}

    private static DozerBeanMapper getMapper() {
        if (mapper == null) {
                mapper = new DozerBeanMapper();
            List<String> dozerMappings = new ArrayList<>();
            dozerMappings.add("dozer/ConverterMappings.xml");
            dozerMappings.add("dozer/FieldMappings.xml");
            mapper.setMappingFiles(dozerMappings);
            mapper.setEventListeners(Stream.of(new GeodesyMLDozerEventListener()).collect(Collectors.toList()));
        }
        return mapper;
    }

    public static <T> T mapWithGuard(Object sourceObject, Class<T> returnClass) {
        if (sourceObject != null) {
            T mapped = getMapper().map(sourceObject, returnClass);
            return mapped;
        } else {
            return null;
        }
    }
}
