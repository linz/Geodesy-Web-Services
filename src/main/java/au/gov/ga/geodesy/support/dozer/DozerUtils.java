package au.gov.ga.geodesy.support.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

public class DozerUtils {

    public static DozerBeanMapper getDozerBeanMapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        List<String> dozerMappings = new ArrayList<>();
        dozerMappings.add("dozer/ConverterMappings.xml");
        dozerMappings.add("dozer/FieldMappings.xml");
        mapper.setMappingFiles(dozerMappings);
        return mapper;
    }

}
