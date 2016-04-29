package au.gov.ga.geodesy.support.mapper.orika;

import java.util.function.Function;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.support.java.util.Isomorphism;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.IgsReceiverModelCodeType;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;

import net.opengis.gml.v_3_2_1.CodeType;

public class GnssReceiverOrikaMapper implements Isomorphism<GnssReceiverType, GnssReceiverLogItem> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapper;

    public GnssReceiverOrikaMapper() {
        mapperFactory.classMap(GnssReceiverLogItem.class, GnssReceiverType.class)
            .fieldMap("type", "igsModelCode").converter("typeConverter").add()
            .field("serialNumber", "manufacturerSerialNumber")
            .fieldMap("satelliteSystem", "satelliteSystem").converter("satelliteSystemConverter").add()
            .byDefault()
            .register();


        ConverterFactory converters = mapperFactory.getConverterFactory();
        converters.registerConverter("typeConverter", new StringToCodeListValueConverter<IgsReceiverModelCodeType>(
            "https://igscb.jpl.nasa.gov/igscb/station/general/rcvr_ant.tab",
            "http://xml.gov.au/icsm/geodesyml/codelists/antenna-receiver-codelists.xml#GeodesyML_GNSSReceiverTypeCode"
        ));
        converters.registerConverter("satelliteSystemConverter",
                new StringToListConverter<CodeType>(
                    new StringToCodeTypeConverter("eGeodesy/satelliteSystem"), TypeFactory.valueOf(CodeType.class)
                )
            );
        converters.registerConverter(new DateToTimePositionConverter());
        mapper = mapperFactory.getMapperFacade();
    }

    public Function<GnssReceiverType, GnssReceiverLogItem> to() {
        return receiver -> mapper.map(receiver, GnssReceiverLogItem.class);
    }

    public Function<GnssReceiverLogItem, GnssReceiverType> from() {
        return receiverLogItem -> mapper.map(receiverLogItem, GnssReceiverType.class);
    }
}
