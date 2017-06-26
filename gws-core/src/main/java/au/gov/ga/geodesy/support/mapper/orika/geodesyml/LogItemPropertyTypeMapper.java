package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.LogItem;
import au.gov.ga.geodesy.support.gml.LogItemPropertyType;
import au.gov.ga.geodesy.support.java.util.Iso;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import net.opengis.gml.v_3_2_1.AbstractGMLType;

/**
 * Reversible mapping between a LogItemProperty type and its target element.
 */
public class LogItemPropertyTypeMapper<P extends LogItemPropertyType, T extends AbstractGMLType, L extends LogItem> implements Iso<P, L> {

    private Iso<P, L> propertyToLogItemMapper;
    private MapperFacade mapper;

    public LogItemPropertyTypeMapper(Iso<T, L> logItemMapper) {
        this.propertyToLogItemMapper = new GMLPropertyTypeMapper<P, T>().compose(logItemMapper);

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(LogItemPropertyType.class, LogItem.class)
            .field("dateInserted", "dateInserted")
            .field("dateDeleted", "dateDeleted")
            .field("deletedReason", "deletedReason")
            .register();

        mapperFactory.getConverterFactory().registerConverter(new InstantToTimePositionConverter());
        this.mapper = mapperFactory.getMapperFacade();
    }

    public L to(P propertyType) {
        L logItem = propertyToLogItemMapper.to(propertyType);
        mapper.map(propertyType, logItem);
        return logItem;
    }

    public P from(L logItem) {
        P propertyType = propertyToLogItemMapper.from(logItem);
        mapper.map(logItem, propertyType);
        return propertyType;
    }
}

