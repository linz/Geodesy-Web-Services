package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.opengis.metadata.citation.ResponsibleParty;

import au.gov.ga.geodesy.domain.model.sitelog.CollocationInformationLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.FormInformation;
import au.gov.ga.geodesy.domain.model.sitelog.FrequencyStandardLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.GnssAntennaLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.LocalEpisodicEffectLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.LogItem;
import au.gov.ga.geodesy.domain.model.sitelog.MoreInformation;
import au.gov.ga.geodesy.domain.model.sitelog.MultipathSourceLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.OtherInstrumentationLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.RadioInterference;
import au.gov.ga.geodesy.domain.model.sitelog.SignalObstructionLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLocation;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.model.sitelog.SurveyedLocalTie;
import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.WaterVaporSensorLogItem;
import au.gov.ga.geodesy.support.gml.GMLPropertyType;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.FormInformationType;
import au.gov.xml.icsm.geodesyml.v_0_4.GnssAntennaPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_4.LocalEpisodicEffectPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_4.MoreInformationType;
import au.gov.xml.icsm.geodesyml.v_0_4.MultipathSourcePropertyType;
import au.gov.xml.icsm.geodesyml.v_0_4.RadioInterferencePropertyType;
import au.gov.xml.icsm.geodesyml.v_0_4.SignalObstructionPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLogType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import net.opengis.gml.v_3_2_1.AbstractGMLType;
import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;

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
            .fieldMap("gnssAntennas", "gnssAntennas").converter("gnssAntennas").add()
            .fieldMap("frequencyStandards", "frequencyStandards").converter("frequencyStandards").add()
            .fieldMap("humiditySensors", "humiditySensors").converter("humiditySensors").add()
            .fieldMap("pressureSensors", "pressureSensors").converter("pressureSensors").add()
            .fieldMap("temperatureSensors", "temperatureSensors").converter("temperatureSensors").add()
            .fieldMap("waterVaporSensors", "waterVaporSensors").converter("waterVaporSensors").add()
            .fieldMap("otherInstrumentations", "otherInstrumentationLogItem").converter("otherInstrumentations").add()
            .fieldMap("signalObstructions", "signalObstructionLogItems").converter("signalObstructions").add()
            .fieldMap("multipathSources", "multipathSourceLogItems").converter("multipathSources").add()
            .fieldMap("localEpisodicEffects", "localEpisodicEffectLogItems").converter("localEpisodicEffects").add()
            .fieldMap("radioInterferences", "radioInterferences").converter("radioInterferences").add()
            .fieldMap("moreInformation", "moreInformation").converter("moreInformation").add()
            .fieldMap("formInformation", "formInformation").converter("formInformation").add()
            .fieldMap("collocationInformations", "collocationInformation").converter("collocationInformations").add()
            .fieldMap("surveyedLocalTies", "surveyedLocalTies").converter("surveyedLocalTies").add()
            .customize(new ResponsiblePartiesMapper())
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

        converters.registerConverter("frequencyStandards",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<FrequencyStandardLogItem>>(
                        logItemsConverter(new FrequencyStandardMapper())
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

        converters.registerConverter("signalObstructions",
                new BidirectionalConverterWrapper<List<SignalObstructionPropertyType>, Set<SignalObstructionLogItem>>(
                        logItemsConverter(new SignalObstructionMapper())
                ) {}
        );

        converters.registerConverter("multipathSources",
                new BidirectionalConverterWrapper<List<MultipathSourcePropertyType>, Set<MultipathSourceLogItem>>(
                        logItemsConverter(new MultipathSourceMapper())
                ) {}
        );


        converters.registerConverter("localEpisodicEffects",
                new BidirectionalConverterWrapper<List<LocalEpisodicEffectPropertyType>, Set<LocalEpisodicEffectLogItem>>(
                        logItemsConverter(new LocalEpisodicEffectMapper())
                ) {}
        );

        converters.registerConverter("radioInterferences",
                new BidirectionalConverterWrapper<List<RadioInterferencePropertyType>, Set<RadioInterference>>(
                        logItemsConverter(new RadioInterferenceMapper())
                ) {}
        );
        converters.registerConverter("gnssAntennas",
                new BidirectionalConverterWrapper<List<GnssAntennaPropertyType>, Set<GnssAntennaLogItem>>(
                        logItemsConverter(new GnssAntennaMapper())
                ) {
                }
        );

        converters.registerConverter("moreInformation",
                new IsoConverter<MoreInformationType, MoreInformation>(new MoreInformationMapper()) {});

        converters.registerConverter("formInformation",
                new IsoConverter<FormInformationType, FormInformation>(new FormInformationMapper()) {});

        converters.registerConverter("collocationInformations",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<CollocationInformationLogItem>>(
                        logItemsConverter(new CollocationInformationMapper())
                ) {}
        );

        converters.registerConverter("surveyedLocalTies",
                new BidirectionalConverterWrapper<List<GMLPropertyType>, Set<SurveyedLocalTie>>(
                        logItemsConverter(new SurveyedLocalTieMapper())
                ) {}
        );

        converters.registerConverter("responsibleParty", new IsoConverter<CIResponsiblePartyType, ResponsibleParty>(new ResponsiblePartyMapper()) {});

        mapper = mapperFactory.getMapperFacade();
    }


    // TODO: refactor and document (work-around for https://github.com/orika-mapper/orika/issues/99)
    public class BidirectionalConverterWrapper<A, B> extends BidirectionalConverter<A, B> {

        private BidirectionalConverter<A, B> delegate;

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
     * Given an equipment isomorphism (from DTO to domain model), return a
     * bidirectional converter from a list of GML equipment property types to a
     * set of domain model collocation information.
     */
    private <P extends GMLPropertyType, T extends AbstractGMLType, L extends Object>
    BidirectionalConverter<List<P>, Set<L>> infoCollectionConverter(Iso<T, L> infoIso) {
        Iso<P, T> propertyIso = new GMLPropertyTypeMapper<>();
        Iso<P, L> elementIso = propertyIso.compose(infoIso);
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
