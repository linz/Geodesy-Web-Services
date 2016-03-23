package au.gov.ga.geodesy.support.mapper.dozer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.igssitelog.domain.model.CollocationInformation;
import au.gov.ga.geodesy.igssitelog.domain.model.FrequencyStandardLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssAntennaLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiverLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.HumiditySensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.LocalEpisodicEvent;
import au.gov.ga.geodesy.igssitelog.domain.model.MoreInformation;
import au.gov.ga.geodesy.igssitelog.domain.model.MultipathSource;
import au.gov.ga.geodesy.igssitelog.domain.model.PressureSensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.RadioInterference;
import au.gov.ga.geodesy.igssitelog.domain.model.SignalObstruction;
import au.gov.ga.geodesy.igssitelog.domain.model.SiteIdentification;
import au.gov.ga.geodesy.igssitelog.domain.model.SiteLocation;
import au.gov.ga.geodesy.igssitelog.domain.model.SurveyedLocalTie;
import au.gov.ga.geodesy.igssitelog.domain.model.TemperatureSensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.WaterVaporSensorLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLSiteLogTranslator;
import au.gov.xml.icsm.geodesyml.v_0_2_2.AgencyPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.BasePossibleProblemSourcesType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.CollocationInformationPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.CollocationInformationType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.FrequencyStandardPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.FrequencyStandardType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GnssAntennaPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GnssAntennaType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GnssReceiverPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.HumiditySensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.HumiditySensorType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.LocalEpisodicEventsPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.LocalEpisodicEventsType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.MoreInformationType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.MultipathSourcesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.ObjectFactory;
import au.gov.xml.icsm.geodesyml.v_0_2_2.OtherInstrumentationPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.OtherInstrumentationType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.PressureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.PressureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.RadioInterferencesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.RadioInterferencesType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SignalObstructionsPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteLogType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SurveyedLocalTiesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SurveyedLocalTiesType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.TemperatureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.TemperatureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.WaterVaporSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.WaterVaporSensorType;

@Service
public class GeodesyMLSiteLogDozerTranslator implements GeodesyMLSiteLogTranslator {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public JAXBElement<GeodesyMLType> dozerTranslate(IgsSiteLog sopacSiteLog) {
        try {
            return run(sopacSiteLog);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new GeodesyRuntimeException("Error trying to translate SopacXML to GeodesyXML", e);
        }
    }

    private JAXBElement<GeodesyMLType> run(IgsSiteLog sopacSiteLog)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        ObjectFactory geodesyObjectFactory = new ObjectFactory();
        GeodesyMLType geodesyMl = new GeodesyMLType();

        JAXBElement<GeodesyMLType> geodesyMLTypeJaxB = geodesyObjectFactory.createGeodesyML(geodesyMl);

        SiteLogType siteLogType = new SiteLogType();

        geodesyMl.getNodeOrAbstractPositionOrPositionPairCovariance()
                .add(geodesyObjectFactory.createSiteLog(siteLogType));

        SiteIdentification siteIdentification = sopacSiteLog.getSiteIdentification();
        SiteIdentificationType siteIdentificationType = DozerDelegate.mapWithGuard(siteIdentification,
                SiteIdentificationType.class);
        siteLogType.setSiteIdentification(siteIdentificationType);

        SiteLocation siteLocation = sopacSiteLog.getSiteLocation();
        SiteLocationType siteLocationType = DozerDelegate.mapWithGuard(siteLocation, SiteLocationType.class);
        siteLogType.setSiteLocation(siteLocationType);

        List<GnssReceiverPropertyType> gnssReceivers = buildSiteLogItem(GnssReceiverPropertyType.class,
                GnssReceiverType.class, GnssReceiverLogItem.class, sopacSiteLog.getGnssReceivers());
        siteLogType.setGnssReceivers(gnssReceivers);

        List<GnssAntennaPropertyType> gnssAntennas = buildSiteLogItem(GnssAntennaPropertyType.class,
                GnssAntennaType.class, GnssAntennaLogItem.class, sopacSiteLog.getGnssAntennas());
        siteLogType.setGnssAntennas(gnssAntennas);

        List<SurveyedLocalTiesPropertyType> surveyedLocalTies = buildSiteLogItem(SurveyedLocalTiesPropertyType.class,
                SurveyedLocalTiesType.class, SurveyedLocalTie.class, sopacSiteLog.getSurveyedLocalTies());
        siteLogType.setSurveyedLocalTies(surveyedLocalTies);

        List<FrequencyStandardPropertyType> frequencyStandards = buildSiteLogItem(FrequencyStandardPropertyType.class,
                FrequencyStandardType.class, FrequencyStandardLogItem.class, sopacSiteLog.getFrequencyStandards());
        siteLogType.setFrequencyStandards(frequencyStandards);

        List<CollocationInformationPropertyType> collocationInformation = buildSiteLogItem(
                CollocationInformationPropertyType.class,
                CollocationInformationType.class, CollocationInformation.class,
                sopacSiteLog.getCollocationInformation());
        siteLogType.setCollocationInformations(collocationInformation);

        List<HumiditySensorPropertyType> humiditySensors = buildSiteLogItem(HumiditySensorPropertyType.class,
                HumiditySensorType.class, HumiditySensorLogItem.class, sopacSiteLog.getHumiditySensors());
        siteLogType.setHumiditySensors(humiditySensors);

        List<PressureSensorPropertyType> pressureSensors = buildSiteLogItem(PressureSensorPropertyType.class,
                PressureSensorType.class, PressureSensorLogItem.class, sopacSiteLog.getPressureSensors());
        siteLogType.setPressureSensors(pressureSensors);

        List<WaterVaporSensorPropertyType> waterSensors = buildSiteLogItem(WaterVaporSensorPropertyType.class,
                WaterVaporSensorType.class, WaterVaporSensorLogItem.class, sopacSiteLog.getWaterVaporSensors());
        siteLogType.setWaterVaporSensors(waterSensors);

        List<TemperatureSensorPropertyType> temperatureSensors = buildSiteLogItem(TemperatureSensorPropertyType.class,
                TemperatureSensorType.class, TemperatureSensorLogItem.class, sopacSiteLog.getTemperatureSensors());
        siteLogType.setTemperatureSensors(temperatureSensors);

        // TODO TEST - No usage in the 684 Sopac SiteLog samples we have
        List<OtherInstrumentationPropertyType> otherInstrumentation = buildSiteLogItem(
                OtherInstrumentationPropertyType.class,
                OtherInstrumentationType.class, CollocationInformation.class, sopacSiteLog.getCollocationInformation());
        siteLogType.setOtherInstrumentations(otherInstrumentation);

        List<RadioInterferencesPropertyType> radioInterference = buildSiteLogItem(RadioInterferencesPropertyType.class,
                RadioInterferencesType.class, RadioInterference.class, sopacSiteLog.getRadioInterferences());
        siteLogType.setRadioInterferencesSet(radioInterference);

        List<MultipathSourcesPropertyType> multipathSource = buildSiteLogItem(MultipathSourcesPropertyType.class,
                BasePossibleProblemSourcesType.class, MultipathSource.class, sopacSiteLog.getMultipathSources());
        siteLogType.setMultipathSourcesSet(multipathSource);

        List<SignalObstructionsPropertyType> signalObstructions = buildSiteLogItem(SignalObstructionsPropertyType.class,
                BasePossibleProblemSourcesType.class, SignalObstruction.class, sopacSiteLog.getSignalObstructions());
        siteLogType.setSignalObstructionsSet(signalObstructions);

        List<LocalEpisodicEventsPropertyType> localEpisodicEvents = buildSiteLogItem(
                LocalEpisodicEventsPropertyType.class, LocalEpisodicEventsType.class, LocalEpisodicEvent.class,
                sopacSiteLog.getLocalEpisodicEvents());
        siteLogType.setLocalEpisodicEventsSet(localEpisodicEvents);

        // IgsSiteLog Contact Agency becomes GeodesyML SiteContact
        AgencyPropertyType siteContact = DozerDelegate.mapWithGuard(sopacSiteLog.getContactAgency(),
                AgencyPropertyType.class);
        siteLogType.setSiteContact(Stream.of(siteContact).collect(Collectors.toList()));

        // IgsSiteLog Responsible Agency becomes GeodesyML Site Metadata Custodian
        AgencyPropertyType siteMetadataCustodian = DozerDelegate.mapWithGuard(sopacSiteLog.getResponsibleAgency(),
                AgencyPropertyType.class);
        siteLogType.setSiteMetadataCustodian(siteMetadataCustodian);

        MoreInformation moreInformation = sopacSiteLog.getMoreInformation();
        MoreInformationType moreInformationType = DozerDelegate.mapWithGuard(moreInformation,
                MoreInformationType.class);
        siteLogType.setMoreInformation(moreInformationType);

        // DataStreams
        // TBD - There is no instance of this across the 682 test files we have

        return geodesyMLTypeJaxB;
    }

    /**
     * Generic method to build List of parentPropertyType that contain the item of interest in a childType. For SiteLogItem (the list
     * becomes a member of that).
     * 
     * @param parentPropertyType
     * @param childType
     * @param sopacSiteLogItems
     *            - list of input data from SopacXML
     * @param mapper
     *            - to map from SopacXML to GeodesyMl
     * @return List of parentPropertyType's or null if the input source data is null or empty
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    private <P, C, S> List<P> buildSiteLogItem(Class<P> parentPropertyType, Class<C> childType, Class<S> sopacItemsType,
            Collection<?> sopacSiteLogItems)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<P> parentPropertyTypesList = new ArrayList<>();

        logger.trace("SiteLog " + parentPropertyType.getName() + " items from SopacXML");
        logger.trace("  sopacSiteLogItems: " + sopacSiteLogItems);
        if (sopacSiteLogItems == null || sopacSiteLogItems.size() == 0) {
            return null;
        }
        for (Object sopacSiteLogItem : sopacSiteLogItems) {
            C newChildType = childType.newInstance();
            newChildType = DozerDelegate.mapWithGuard(sopacSiteLogItem, childType);
            logger.trace("  " + newChildType);
            Object newParentPropertyType = parentPropertyType.newInstance();
            setBasedOnChildType(newParentPropertyType, newChildType);
            parentPropertyTypesList.add((P) newParentPropertyType);
        }

        return parentPropertyTypesList;
    }

    /**
     * Use reflection to find the setter in newParentPropertyType.getClass() class that takes the type newChildType.getClass() and run the
     * setter.
     * 
     * @param newParentPropertyType
     * @param newChildType
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    void setBasedOnChildType(Object newParentPropertyType, Object newChildType)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method[] methods = newParentPropertyType.getClass().getMethods();
        List<Method> theSetterMethod = Arrays.stream(methods).filter(m -> m.getName().startsWith("set")
                && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(newChildType.getClass()))
                .collect(Collectors.toList());
        if (theSetterMethod.size() == 0) {
            throw new GeodesyRuntimeException("Expecting a setter method on: " + newParentPropertyType.getClass()
                    + ", that takes the type: " + newChildType.getClass());
        }
        theSetterMethod.get(0).invoke(newParentPropertyType, newChildType);
    }

}
