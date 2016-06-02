package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.MoreInformation;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Reversible mapping between GeodesyML MoreInformationType DTO and
 * MoreInformation site log entity.
 */
public class MoreInformationMapper implements Iso<MoreInformationType, MoreInformation> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public MoreInformationMapper() {
        mapperFactory.classMap(MoreInformationType.class, MoreInformation.class)
                .field("dataCenter[0]", "primaryDataCenter")
                .field("dataCenter[1]", "secondaryDataCenter")
                .fieldMap("DOI", "doi").converter("codeTypeConverter").add()
                .byDefault()
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("codeTypeConverter", new StringToCodeTypeConverter("eGeodesy/doi") {});

        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public MoreInformation to(MoreInformationType moreInformationType) {
        return mapper.map(moreInformationType, MoreInformation.class);
    }

    /**
     * {@inheritDoc}
     */
    public MoreInformationType from(MoreInformation moreInformation) {
        return mapper.map(moreInformation, MoreInformationType.class);
    }
}
