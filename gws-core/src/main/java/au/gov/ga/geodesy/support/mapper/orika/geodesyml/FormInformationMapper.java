package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.FormInformation;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.FormInformationType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Reversible mapping between GeodesyML FormInformationType DTO and
 * FormInformation site log entity.
 */
public class FormInformationMapper implements Iso<FormInformationType, FormInformation> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private MapperFacade mapper;

    public FormInformationMapper() {
        mapperFactory.classMap(FormInformationType.class, FormInformation.class)
                .byDefault()
                .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter(new InstantToTimePositionConverter());
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * {@inheritDoc}
     */
    public FormInformation to(FormInformationType formInfoType) {
        return mapper.map(formInfoType, FormInformation.class);
    }

    /**
     * {@inheritDoc}
     */
    public FormInformationType from(FormInformation formInfo) {
        return mapper.map(formInfo, FormInformationType.class);
    }
}
