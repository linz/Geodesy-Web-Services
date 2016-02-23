package au.gov.ga.geodesy.domain.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.igssitelog.domain.model.FrequencyStandardLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssAntennaLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiverLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.HumiditySensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.LocalEpisodicEvent;
import au.gov.ga.geodesy.igssitelog.domain.model.PressureSensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.SiteIdentification;
import au.gov.ga.geodesy.igssitelog.domain.model.SiteLocation;
import au.gov.ga.geodesy.igssitelog.domain.model.SurveyedLocalTie;
import au.gov.ga.geodesy.igssitelog.domain.model.TemperatureSensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.WaterVaporSensorLogItem;
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
import au.gov.xml.icsm.geodesyml.v_0_2_2.ObjectFactory;
import au.gov.xml.icsm.geodesyml.v_0_2_2.PressureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.PressureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteLogType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SurveyedLocalTiesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.SurveyedLocalTiesType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.TemperatureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.TemperatureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.WaterVaporSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.WaterVaporSensorType;
import net.opengis.gml.v_3_2_1.CodeType;

@Service
public class GeodesyMLSiteLogTranslator {

    public GeodesyMLType dozerTranslate(IgsSiteLog sopacSiteLog) {
        try {
            return run(sopacSiteLog);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new GeodesyRuntimeException("Error trying to translate SopacXML to GeodesyXML", e);
        }
    }

    private GeodesyMLType run(IgsSiteLog sopacSiteLog)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        DozerBeanMapper mapper = new DozerBeanMapper();
        List<String> dozerMappings = new ArrayList<>();
        dozerMappings.add("dozer/ConverterMappings.xml");
        dozerMappings.add("dozer/FieldMappings.xml");
        mapper.setMappingFiles(dozerMappings);

        ObjectFactory geodesyObjectFactory = new ObjectFactory();
        net.opengis.gml.v_3_2_1.ObjectFactory gmlObjectFactory = new net.opengis.gml.v_3_2_1.ObjectFactory();
        GeodesyMLType geodesyMl = new GeodesyMLType();
        SiteLogType siteLogType = new SiteLogType();

        geodesyMl.getNodeOrAbstractPositionOrPositionPairCovariance()
                .add(geodesyObjectFactory.createSiteLog(siteLogType));

        SiteIdentification siteIdentification = sopacSiteLog.getSiteIdentification();
        SiteIdentificationType siteIdentificationType = mapper.map(siteIdentification, SiteIdentificationType.class);
        siteLogType.setSiteIdentification(siteIdentificationType);

        SiteLocation siteLocation = sopacSiteLog.getSiteLocation();
        SiteLocationType siteLocationType = mapper.map(siteLocation, SiteLocationType.class);
        siteLogType.setSiteLocation(siteLocationType);

        List<GnssReceiverPropertyType> gnssReceivers = buildSiteLogItem(GnssReceiverPropertyType.class,
                GnssReceiverType.class, GnssReceiverLogItem.class, sopacSiteLog.getGnssReceivers(), mapper);
        siteLogType.setGnssReceivers(gnssReceivers);

        List<GnssAntennaPropertyType> gnssAntennas = buildSiteLogItem(GnssAntennaPropertyType.class,
                GnssAntennaType.class, GnssAntennaLogItem.class, sopacSiteLog.getGnssAntennas(), mapper);
        siteLogType.setGnssAntennas(gnssAntennas);

        List<SurveyedLocalTiesPropertyType> surveyedLocalTies = buildSiteLogItem(SurveyedLocalTiesPropertyType.class,
                SurveyedLocalTiesType.class, SurveyedLocalTie.class, sopacSiteLog.getSurveyedLocalTies(), mapper);
        siteLogType.setSurveyedLocalTies(surveyedLocalTies);

        List<FrequencyStandardPropertyType> frequencyStandards = buildSiteLogItem(FrequencyStandardPropertyType.class,
                FrequencyStandardType.class, FrequencyStandardLogItem.class, sopacSiteLog.getFrequencyStandards(),
                mapper);
        siteLogType.setFrequencyStandards(frequencyStandards);

        List<HumiditySensorPropertyType> humiditySensors = buildSiteLogItem(HumiditySensorPropertyType.class,
                HumiditySensorType.class, HumiditySensorLogItem.class, sopacSiteLog.getHumiditySensors(), mapper);
        siteLogType.setHumiditySensors(humiditySensors);

        List<PressureSensorPropertyType> pressureSensors = buildSiteLogItem(PressureSensorPropertyType.class,
                PressureSensorType.class, PressureSensorLogItem.class, sopacSiteLog.getPressureSensors(), mapper);
        siteLogType.setPressureSensors(pressureSensors);

        List<WaterVaporSensorPropertyType> waterSensors = buildSiteLogItem(WaterVaporSensorPropertyType.class,
                WaterVaporSensorType.class, WaterVaporSensorLogItem.class, sopacSiteLog.getWaterVaporSensors(), mapper);
        siteLogType.setWaterVaporSensors(waterSensors);

        List<TemperatureSensorPropertyType> temperatureSensors = buildSiteLogItem(TemperatureSensorPropertyType.class,
                TemperatureSensorType.class, TemperatureSensorLogItem.class, sopacSiteLog.getTemperatureSensors(),
                mapper);
        siteLogType.setTemperatureSensors(temperatureSensors);

        List<LocalEpisodicEventsPropertyType> localEpisodicEvents = buildSiteLogItem(
                LocalEpisodicEventsPropertyType.class, LocalEpisodicEventsType.class, LocalEpisodicEvent.class,
                sopacSiteLog.getLocalEpisodicEvents(), mapper);
        siteLogType.setLocalEpisodicEventsSet(localEpisodicEvents);

        // +++++
        List<CodeType> names = new ArrayList<>();
        CodeType nameCT = gmlObjectFactory.createCodeType();
        nameCT.setValue("GeodesyML Rocks Like Axel Foley!");
        names.add(nameCT);
        geodesyMl.setName(names);

        return geodesyMl;
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
     * @return List of parentPropertyType's
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    private <P, C, S> List<P> buildSiteLogItem(Class<P> parentPropertyType, Class<C> childType, Class<S> sopacItemsType,
            Collection<?> sopacSiteLogItems, DozerBeanMapper mapper)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<P> parentPropertyTypesList = new ArrayList<>();

        System.out.println("SiteLog " + parentPropertyType.getName() + " items from SopacXML");
        for (Object sopacSiteLogItem : sopacSiteLogItems) {
            C newChildType = childType.newInstance();
            newChildType = mapper.map(sopacSiteLogItem, childType);
            System.out.println("  " + newChildType);
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
