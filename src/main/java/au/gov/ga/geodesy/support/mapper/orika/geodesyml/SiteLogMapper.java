package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.GnssAntennaLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.LocalEpisodicEventLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.LogItem;
import au.gov.ga.geodesy.domain.model.sitelog.MultipathSourceLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.OtherInstrumentationLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.RadioInterference;
import au.gov.ga.geodesy.domain.model.sitelog.SignalObstructionLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLocation;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.WaterVaporSensorLogItem;
import au.gov.ga.geodesy.support.gml.GMLPropertyType;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.LocalEpisodicEventsPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.MultipathSourcesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.RadioInterferencesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.SignalObstructionsPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import net.opengis.gml.v_3_2_1.AbstractGMLType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            .fieldMap("humiditySensors", "humiditySensors").converter("humiditySensors").add()
            .fieldMap("pressureSensors", "pressureSensors").converter("pressureSensors").add()
            .fieldMap("temperatureSensors", "temperatureSensors").converter("temperatureSensors").add()
            .fieldMap("waterVaporSensors", "waterVaporSensors").converter("waterVaporSensors").add()
            .fieldMap("otherInstrumentations", "otherInstrumentationLogItem").converter("otherInstrumentations").add()
            .fieldMap("signalObstructionsSet", "signalObstructionLogItems").converter("signalObstructionsSet").add()
            .fieldMap("multipathSourcesSet", "multipathSourceLogItems").converter("multipathSourcesSet").add()
            .fieldMap("localEpisodicEventsSet", "localEpisodicEventLogItems").converter("localEpisodicEventsSet").add()
            .fieldMap("radioInterferencesSet", "radioInterferences").converter("radioInterferencesSet").add()
            .fieldMap("gnssAntennas", "gnssAntennas").converter("gnssAntennas").add()
            /* .byDefault() */
            .register();

        ConverterFactory converters = mapperFactory.getConverterFactory();

        converters.registerConverter("siteIdentification",
                new IsoConverter<SiteIdentificationType, SiteIdentification>(new SiteIdentificationMapper()) {});

        converters.registerConverter("siteLocation",
                new IsoConverter<SiteLocationType, SiteLocation>(new SiteLocationMapper()) {});

        converters.registerConverter("gnssReceivers",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<GnssReceiverLogItem>>(
                        logItemsConverter(new GnssReceiverMapper())
                ) {}
        );

        converters.registerConverter("humiditySensors",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<HumiditySensorLogItem>>(
                        logItemsConverter(new HumiditySensorMapper())
                ) {}
        );

        converters.registerConverter("pressureSensors",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<PressureSensorLogItem>>(
                        logItemsConverter(new PressureSensorMapper())
                ) {}
        );

        converters.registerConverter("temperatureSensors",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<TemperatureSensorLogItem>>(
                        logItemsConverter(new TemperatureSensorMapper())
                ) {}
        );

        converters.registerConverter("waterVaporSensors",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<WaterVaporSensorLogItem>>(
                        logItemsConverter(new WaterVaporSensorMapper())
                ) {}
        );

        converters.registerConverter("otherInstrumentations",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<OtherInstrumentationLogItem>>(
                        logItemsConverter(new OtherInstrumentationMapper())
                ) {}
        );

        converters.registerConverter("signalObstructionsSet",
                new BidirectionalConverterWrapper<List<SignalObstructionsPropertyType>, Set<SignalObstructionLogItem>>(
                        logItemsConverter(new SignalObstructionMapper())
                ) {}
        );

        converters.registerConverter("multipathSourcesSet",
                new BidirectionalConverterWrapper<List<MultipathSourcesPropertyType>, Set<MultipathSourceLogItem>>(
                        logItemsConverter(new MultipathSourcesMapper())
                ) {}
        );

        converters.registerConverter("localEpisodicEventsSet",
                new BidirectionalConverterWrapper<List<LocalEpisodicEventsPropertyType>, Set<LocalEpisodicEventLogItem>>(
                        logItemsConverter(new LocalEpisodicEventMapper())
                ) {}
        );

        converters.registerConverter("radioInterferencesSet",
                new BidirectionalConverterWrapper<List<RadioInterferencesPropertyType>, Set<RadioInterference>>(
                        logItemsConverter(new RadioInterferenceMapper())
                ) {}
        );
        converters.registerConverter("gnssAntennas",
                new BidirectionalConverterWrapper<List<GnssAntennaPropertyType>, Set<GnssAntennaLogItem>>(
                        logItemsConverter(new GnssAntennaMapper())
                ) {
                }
        );

        mapper = mapperFactory.getMapperFacade();
    }


    // TODO: refactor and document
    public class BidirectionalConverterWrapper<A, B> extends BidirectionalConverter<A, B> {

        private BidirectionalConverter<A ,B> delegate;

        public BidirectionalConverterWrapper(BidirectionalConverter<A, B> delegate) {
            this.delegate = delegate;
        }

        @Override
        public B convertTo(A a, Type<B> type, MappingContext mappingContext) {
            return delegate.convertTo(a, type, mappingContext);
        }

        @Override
        public A convertFrom(B b, Type<A> type, MappingContext mappingContext) {
            return delegate.convertFrom(b, type, mappingContext);
        }
    }


    /**
     * Given a GMLPropertyType isomorphism (from DTO to domain model), return a
     * bidirectional converter from a list of GML property types to a
     * set of domain model log items.
     */
    private <P extends GMLPropertyType, T extends AbstractGMLType, L extends LogItem>
    BidirectionalConverter<List<P>, Set<L>> logItemsConverter(Iso<T, L> logItemsIso) {

        Iso<P, T> propertyIso = new GMLPropertyTypeMapper<>();
        Iso<P, L> elementIso = propertyIso.compose(logItemsIso);
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
