package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.MoreInformation;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
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
                .fieldBToA("primaryDataCenter", "dataCenter[0]")
                .fieldBToA("secondaryDataCenter", "dataCenter[1]")
                .customize(new CustomMapper<MoreInformationType, MoreInformation>() {
                       @Override
                       public void mapAtoB(MoreInformationType infoType, MoreInformation info, MappingContext ctx) {
                           if (infoType.getDataCenter().size() >= 1) {
                               info.setPrimaryDataCenter(infoType.getDataCenter().get(0));
                           }
                           if (infoType.getDataCenter().size() >= 2) {
                               info.setSecondaryDataCenter(infoType.getDataCenter().get(1));
                           }
                       }
                })
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
