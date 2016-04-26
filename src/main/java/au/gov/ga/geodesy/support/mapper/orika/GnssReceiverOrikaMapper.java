package au.gov.ga.geodesy.support.mapper.orika;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;

import net.opengis.gml.v_3_2_1.CodeType;

public class GnssReceiverOrikaMapper {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapper;

    public GnssReceiverOrikaMapper() {
        mapperFactory.classMap(GnssReceiverLogItem.class, GnssReceiverType.class)
            .field("serialNumber", "manufacturerSerialNumber")
            .fieldMap("satelliteSystem", "satelliteSystem").converter("satelliteSystemConverter").add()
            .byDefault()
            .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("satelliteSystemConverter",
                new StringToListConverter<CodeType>(
                    new StringToCodeTypeConverter("eGeodesy/satelliteSystem"), TypeFactory.valueOf(CodeType.class)
                )
            );
        converters.registerConverter(new DateToTimePositionConverter());
        mapper = mapperFactory.getMapperFacade();
    }

    public GnssReceiverLogItem mapFromDto(GnssReceiverType receiver) {
        return mapper.map(receiver, GnssReceiverLogItem.class);
    }

    public GnssReceiverType mapToDto(GnssReceiverLogItem logItem) {
        return mapper.map(logItem, GnssReceiverType.class);
    }
}
