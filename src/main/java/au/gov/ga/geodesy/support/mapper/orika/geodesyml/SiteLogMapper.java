package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import au.gov.ga.geodesy.domain.model.sitelog.EquipmentLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLocation;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.ga.geodesy.support.gml.GMLPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import net.opengis.gml.v_3_2_1.AbstractGMLType;

/**
 * Reversible mapping between GeodesyML SiteLogType DTO and
 * SiteLog site log entity.
 */
public class SiteLogMapper implements Iso<SiteLogType, SiteLog> {

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapper;

    public SiteLogMapper() {
        mapperFactory.classMap(SiteLogType.class, SiteLog.class)
            .fieldMap("siteIdentification", "siteIdentification").converter("siteIdentification").add()
            .fieldMap("siteLocation", "siteLocation").converter("siteLocation").add()
            .fieldMap("gnssReceivers", "gnssReceivers").converter("gnssReceivers").add()
            /* .byDefault() */
            .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();

        converters.registerConverter("siteIdentification",
                new IsoConverter<SiteIdentificationType, SiteIdentification>(new SiteIdentificationMapper()) {});

        converters.registerConverter("siteLocation",
                new IsoConverter<SiteLocationType, SiteLocation>(new SiteLocationMapper()) {});

        converters.registerConverter("gnssReceivers", equipmentCollectionConverter(new GnssReceiverMapper()));

        mapper = mapperFactory.getMapperFacade();
    }


    /**
     * Given an equipment isomorphism (from DTO to domain model), return a
     * bidirectional converter from a list of GML equipment property types to a
     * set of domain model equipment log items.
     */
    private <P extends GMLPropertyType, T extends AbstractGMLType, L extends EquipmentLogItem>
    BidirectionalConverter<List<P>, Set<L>> equipmentCollectionConverter(Iso<T, L> equipmentIso) {

        Iso<P, T> propertyIso = new GMLPropertyTypeMapper<>();
        Iso<P, L> elementIso = propertyIso.compose(equipmentIso);
        return new IsoConverter<>(new ListToSet<>(elementIso));
    }

    /**
     * Given an isomorphism from A to B, return an isomorphism from a set of A
     * to a set B.
     */
    private class ListToSet<A, B> implements Iso<List<A>, Set<B>> {

        private Iso<A, B> elementIso;

        public ListToSet(Iso<A, B> elementIso) {
            this.elementIso = elementIso;
        }

        public Set<B> to(List<A> list) {
            return list.stream().map(elementIso::to).collect(Collectors.toSet());
        }

        public List<A> from(Set<B> set) {
            return set.stream().map(elementIso::from).collect(Collectors.toList());
        }
    }

    /**
     * {@inheritDoc}
     */
    public SiteLog to(SiteLogType siteLogType) {
        return mapper.map(siteLogType, SiteLog.class);
    }

    /**
     * {@inheritDoc}
     */
    public SiteLogType from(SiteLog siteLog) {
        return mapper.map(siteLog, SiteLogType.class);
    }
}
