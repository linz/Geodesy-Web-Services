package au.gov.ga.geodesy.support.dozer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.interfaces.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.interfaces.geodesyml.MarshallingException;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.dozer.GeodesyMLSiteLogDozerTranslator;
import au.gov.ga.geodesy.support.dozer.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
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

// @ContextConfiguration(classes = {GeodesyServiceTestConfig.class}, loader = AnnotationConfigContextLoader.class)
// @Transactional("geodesyTransactionManager")
public class TranslateTest { // extends AbstractTestNGSpringContextTests {
    // @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    // @Autowired
    private GeodesyMLSiteLogDozerTranslator geodesyMLSiteLogTranslator;

    // @Autowired
    private GeodesyMLMarshaller geodesyMLMarshaller;

    @BeforeClass
    public void startup()
            throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException {
        marshaller = new IgsSiteLogMoxyMarshaller();
        geodesyMLSiteLogTranslator = new GeodesyMLSiteLogDozerTranslator();
        geodesyMLMarshaller = new GeodesyMLMoxy();
    }

    /**
     * @param fileName
     * @return file with name fileName in some constant location.
     * @throws IOException
     */
    private Path returnTestFile(String fileName) throws IOException {
        String tempDirStrName = "/tmp/geodesyml";

        Path tempDirName = Paths.get(tempDirStrName);
        Path tempDir = null;
        if (Files.exists(tempDirName)) {
            tempDir = tempDirName;
        } else {
            tempDir = Files.createDirectory(tempDirName);
        }
        Path tempFileName = tempDir.resolve("marshalled.xml");
        Path tempFile = null;
        if (Files.exists(tempFileName)) {
            tempFile = tempFileName;
        } else {
            tempFile = Files.createFile(tempFileName);
        }
        return tempFile;
    }

    @Test
    public void testTranslate() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        String source = "sitelog/ALIC.xml";
        String destTmpName = "ALICGML.xml";

        Reader input = new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(source));

        IgsSiteLog siteLog = marshaller.unmarshal(input);

        JAXBElement<GeodesyMLType> geodesyMLJAXB = geodesyMLSiteLogTranslator.dozerTranslate(siteLog);

        GeodesyMLType geodesyML = geodesyMLJAXB.getValue();

        Assert.assertNotNull(geodesyML);

        System.out.println("Write xml file to: " + returnTestFile(destTmpName).toString());
        FileWriter writer = new FileWriter(returnTestFile(destTmpName).toFile());
        geodesyMLMarshaller.marshal(geodesyMLJAXB, writer);

        Assert.assertEquals(geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().size(), 1);
        Assert.assertTrue(geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().get(0) instanceof JAXBElement);

        Stream<SiteLogType> siteLogTypeStream = GeodesyMLUtils.getElementFromJAXBElements(
                geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance(), SiteLogType.class);

        SiteLogType siteLogType = siteLogTypeStream.collect(Collectors.toList()).get(0);

        SiteIdentificationType siteIdentificationType = siteLogType.getSiteIdentification();
        SiteLocationType siteLocationType = siteLogType.getSiteLocation();

        Assert.assertEquals(siteIdentificationType.getMonumentDescription().getValue(), "PILLAR");
        Assert.assertEquals(Double.parseDouble(siteLocationType.getApproximatePositionITRF().getXCoordinateInMeters()),
                Double.parseDouble("-4052051.7670"));

        // Receivers
        List<GnssReceiverPropertyType> receivers = siteLogType.getGnssReceivers();
        Assert.assertEquals(12, receivers.size());

        Collections.sort(receivers, (r1, r2) -> r1.getGnssReceiver().getFirmwareVersion()
                .compareTo(r2.getGnssReceiver().getFirmwareVersion()));
        GnssReceiverType receiver1 = receivers.get(0).getGnssReceiver();
        // Test some required fields
        Assert.assertEquals(receiver1.getReceiverType().getValue(), "AOA ICS-4000Z");
        Assert.assertEquals(receiver1.getSatelliteSystem().get(0).getValue(), "GPS");
        Assert.assertEquals(receiver1.getSerialNumber(), "C128");
        Assert.assertEquals(receiver1.getFirmwareVersion(), "3.2.32.9");
        Assert.assertEquals(receiver1.getElevationCutoffSetting(), "0");
        Assert.assertEquals(receiver1.getDateInstalled().getValue().get(0), "31 Jul 1999 01:00 GMT");
        Assert.assertEquals(receiver1.getDateRemoved().getValue().get(0), "14 Jan 2000 01:50 GMT");
        Assert.assertEquals(receiver1.getTemperatureStabilization(), "none");
        Assert.assertEquals(receiver1.getNotes(), "Upgrade of firmware to avoid GPS week");

        // Antennas
        List<GnssAntennaPropertyType> antennas = siteLogType.getGnssAntennas();
        Assert.assertEquals(antennas.size(), 3);

        Collections.sort(antennas, (a1, a2) -> a1.getGnssAntenna().getAntennaType().getValue()
                .compareTo(a2.getGnssAntenna().getAntennaType().getValue()));
        GnssAntennaType antenna1 = antennas.get(0).getGnssAntenna();
        // Test some required fields
        Assert.assertEquals(antenna1.getAntennaType().getValue(), "AOAD/M_T AUST");
        Assert.assertEquals(antenna1.getSerialNumber(), "318");
        Assert.assertEquals(antenna1.getAntennaReferencePoint().getValue(), "BPA");
        Assert.assertEquals(antenna1.getMarkerArpUpEcc(), "0.007");
        Assert.assertEquals(antenna1.getMarkerArpNorthEcc(), "0.0");
        Assert.assertEquals(antenna1.getMarkerArpEastEcc(), "0.0");
        Assert.assertEquals(antenna1.getAlignmentFromTrueNorth(), "0");
        Assert.assertEquals(antenna1.getAntennaRadomeType().getValue(), "AUST");
        Assert.assertEquals(antenna1.getDateInstalled().getValue().get(0), "15 May 1994 00:00 GMT");
        Assert.assertEquals(antenna1.getDateRemoved().getValue().get(0), "15 Jun 2003 03:30 GMT");
        Assert.assertEquals(antenna1.getNotes(), "Radome was damaged at an unknown time during this period.");

        // SurveyedLocalTiesPropertyType
        List<SurveyedLocalTiesPropertyType> surveyedLocalTies = siteLogType.getSurveyedLocalTies();
        Assert.assertEquals(surveyedLocalTies.size(), 3);

        Collections.sort(surveyedLocalTies, (s1, s2) -> s1.getSurveyedLocalTies().getTiedMarkerName()
                .compareTo(s2.getSurveyedLocalTies().getTiedMarkerName()));
        SurveyedLocalTiesType surveyedTies1 = surveyedLocalTies.get(0).getSurveyedLocalTies();
        // Test some required fields
        Assert.assertEquals(surveyedTies1.getTiedMarkerName(), "RM1");
        Assert.assertEquals(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDx(), "-15.366");
        Assert.assertEquals(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDy(), "-9.632");
        Assert.assertEquals(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDz(), "9.099");
        Assert.assertEquals(surveyedTies1.getLocalSiteTiesAccuracy(), "+-1mm");
        Assert.assertEquals(surveyedTies1.getDateMeasured().getValue().get(0), "11 Aug 1992 14:00 GMT");

        // FrequencyStandardPropertyType
        List<FrequencyStandardPropertyType> frequencyStandards = siteLogType.getFrequencyStandards();
        Assert.assertEquals(frequencyStandards.size(), 1);

        FrequencyStandardType frequencyStandardType = frequencyStandards.get(0).getFrequencyStandard();
        // Test some required fields
        Assert.assertEquals(frequencyStandardType.getStandardType().getValue(), "QUARTZ/INTERNAL");
        Assert.assertEquals(GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                .getTheTimePeriodType(frequencyStandardType.getValidTime()).getBeginPosition().getValue().get(0)),
                "15 May 1994 00:00 GMT");

        // Humidity Sensors
        List<HumiditySensorPropertyType> humiditySensors = siteLogType.getHumiditySensors();
        Assert.assertEquals(humiditySensors.size(), 1);

        HumiditySensorType humiditySensor = humiditySensors.get(0).getHumiditySensor();
        // Test some required fields
        Assert.assertEquals(humiditySensor.getType().getValue(), "PAROSCIENTIFIC MET3A");
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(humiditySensor.getValidTime()).getBeginPosition().getValue().get(0)),
                "29 Mar 2006 00:00 GMT");
        Assert.assertEquals(humiditySensor.getSerialNumber(), "98850");
        Assert.assertEquals(humiditySensor.getHeightDiffToAntenna(), "2.4");
        Assert.assertEquals(humiditySensor.getDataSamplingInterval(), "30");
        Assert.assertEquals(humiditySensor.getAccuracyPercentRelativeHumidity(), "2% rel h");
        Assert.assertEquals(humiditySensor.getAspiration(), "FAN");

        // Pressuer Sensors
        List<PressureSensorPropertyType> pressureSensors = siteLogType.getPressureSensors();
        Assert.assertEquals(pressureSensors.size(), 1);

        PressureSensorType pressureSensor = pressureSensors.get(0).getPressureSensor();
        // Test some required fields
        Assert.assertEquals(pressureSensor.getType().getValue(), "PAROSCIENTIFIC MET3A");
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(pressureSensor.getValidTime()).getBeginPosition().getValue().get(0)),
                "29 Mar 2006 00:00 GMT");
        Assert.assertEquals(pressureSensor.getManufacturer(), "Paroscientific, Inc.");
        Assert.assertEquals(pressureSensor.getSerialNumber(), "98850");
        Assert.assertEquals(pressureSensor.getHeightDiffToAntenna(), "2.4");
        Assert.assertEquals(pressureSensor.getDataSamplingInterval(), "30");
        Assert.assertEquals(pressureSensor.getAccuracyHPa(), "0.1 mbar");

        // Water Vapour Sensors
        List<WaterVaporSensorPropertyType> waterVapourSensors = siteLogType.getWaterVaporSensors();
        Assert.assertEquals(waterVapourSensors.size(), 1);

        WaterVaporSensorType waterVapourSensor = waterVapourSensors.get(0).getWaterVaporSensor();
        // Test some required fields
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(waterVapourSensor.getValidTime()).getBeginPosition().getValue().get(0)),
                "29 Mar 2006 00:00 GMT");

        // Temperature Sensors
        List<TemperatureSensorPropertyType> temperatureSensors = siteLogType.getTemperatureSensors();
        Assert.assertEquals(temperatureSensors.size(), 1);

        TemperatureSensorType temperatureSensor = temperatureSensors.get(0).getTemperatureSensor();
        // Test some required fields
        Assert.assertEquals(temperatureSensor.getType().getValue(), "PAROSCIENTIFIC MET3A");
        Assert.assertEquals(temperatureSensor.getManufacturer(), "Paroscientific, Inc.");
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(temperatureSensor.getValidTime()).getBeginPosition().getValue().get(0)),
                "29 Mar 2006 00:00 GMT");
        Assert.assertEquals(temperatureSensor.getSerialNumber(), "98850");
        Assert.assertEquals(temperatureSensor.getHeightDiffToAntenna(), "2.4");
        Assert.assertEquals(temperatureSensor.getDataSamplingInterval(), "30");
        Assert.assertEquals(temperatureSensor.getAccuracyDegreesCelcius(), "0.5 deg C");
        Assert.assertEquals(temperatureSensor.getAspiration(), "FAN");

        // Local Episodic Events
        List<LocalEpisodicEventsPropertyType> localEpisodicEvents = siteLogType.getLocalEpisodicEventsSet();
        Assert.assertEquals(localEpisodicEvents.size(), 1);

        LocalEpisodicEventsType localEpisodicEvent = localEpisodicEvents.get(0).getLocalEpisodicEvents();
        // Test some required fields
        Assert.assertEquals(localEpisodicEvent.getEvent(), "TREE CLEARING");
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimeInstantType(localEpisodicEvent.getValidTime()).getTimePosition().getValue().get(0)),
                "20 Jul 2011");

    }

}
