package au.gov.ga.geodesy.support.mapper.dozer;

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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.interfaces.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.interfaces.geodesyml.MarshallingException;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.BasePossibleProblemSourcesType;
import au.gov.xml.icsm.geodesyml.v_0_3.CollocationInformationPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.FormInformationType;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.LocalEpisodicEventsPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.LocalEpisodicEventsType;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;
import au.gov.xml.icsm.geodesyml.v_0_3.MultipathSourcesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.RadioInterferencesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.RadioInterferencesType;
import au.gov.xml.icsm.geodesyml.v_0_3.SignalObstructionsPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteIdentificationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import au.gov.xml.icsm.geodesyml.v_0_3.SurveyedLocalTiesPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.SurveyedLocalTiesType;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.WaterVaporSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.WaterVaporSensorType;
import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;

// @ContextConfiguration(classes = {GeodesyServiceTestConfig.class}, loader = AnnotationConfigContextLoader.class)
// @Transactional("geodesyTransactionManager")
public class TranslateTest { // extends AbstractTestNGSpringContextTests {
    /**
     * Location of input test data - original location of files that haven't been modified
     */
    private final static String SITEDATADIR = "sitelog";
    /**
     * Location of input test data - same as that in SITEDATADIR though modified in some way to improve or fix test
     */
    private final static String TESTDATADIR = "sitelog/testData";

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
        Path tempFileName = tempDir.resolve(fileName);
        Path tempFile = null;
        if (Files.exists(tempFileName)) {
            tempFile = tempFileName;
        } else {
            tempFile = Files.createFile(tempFileName);
        }
        return tempFile;
    }

    /**
     * @param inputResourceDir
     *            - directory in the resources in which the input file exists
     * @param inputFile
     *            - file to test
     * @return
     * @throws MarshallingException
     * @throws IOException
     * @throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException
     * @throws ParseException
     */
    public GeodesyMLType testTranslate(String inputResourceDir, String inputFile) throws MarshallingException,
            IOException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        String source = inputResourceDir + "/" + inputFile + ".xml";
        String destTmpName = inputFile + ".out.xml";

        Reader input = new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(source));

        IgsSiteLog siteLog = marshaller.unmarshal(input);

        JAXBElement<GeodesyMLType> geodesyMLJAXB = geodesyMLSiteLogTranslator.dozerTranslate(siteLog);

        GeodesyMLType geodesyML = geodesyMLJAXB.getValue();

        Assert.assertNotNull(geodesyML);

        System.out.println("Write xml file to: " + returnTestFile(destTmpName).toString());
        FileWriter writer = new FileWriter(returnTestFile(destTmpName).toFile());
        geodesyMLMarshaller.marshal(geodesyMLJAXB, writer);

        return geodesyML;
    }

    private SiteLogType getSiteLog(GeodesyMLType geodesyML) {
        Assert.assertEquals(geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().size(), 1);
        Assert.assertTrue(geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance().get(0) instanceof JAXBElement);

        Stream<SiteLogType> siteLogTypeStream = GeodesyMLUtils.getElementFromJAXBElements(
                geodesyML.getNodeOrAbstractPositionOrPositionPairCovariance(), SiteLogType.class);

        SiteLogType siteLogType = siteLogTypeStream.collect(Collectors.toList()).get(0);
        return siteLogType;
    }

    @Test
    public void testALIC() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TESTDATADIR, "ALIC");

        SiteLogType siteLogType = getSiteLog(geodesyML);

        FormInformationType formIdentificationType = siteLogType.getFormInformation();
        Assert.assertEquals(formIdentificationType.getPreparedBy(), "Nick Dando");

        // Input is 2013-07-08 and because there is no time-zone, it gets the local one (8 July 2013 +10 or +11)
        // GMT (the dateFormat uses forces GMT) will be the day before and due to daylight savings ambiguity I won't
        // check the time
        MatcherAssert.assertThat("formIdentificationType.getDatePrepared()",
                formIdentificationType.getDatePrepared().getValue().get(0), Matchers.startsWith("2013-07-07"));

        Assert.assertEquals(formIdentificationType.getReportType(), "UPDATE");
        // These don't exist (should they - Mmm, not in Sopac SiteLog either so no)?
        // Assert.assertEquals(formIdentificationType.getPreviousLog(), "alic_20130308.log");
        // Assert.assertEquals(formIdentificationType.getModifiedSections(), "3.11, 3.12");

        // <mi:previousLog>alic_20130308.log</mi:previousLog>
        // <mi:modifiedSections>3.11, 3.12</mi:modifiedSections>

        SiteIdentificationType siteIdentificationType = siteLogType.getSiteIdentification();
        Assert.assertEquals(siteIdentificationType.getMonumentDescription().getValue(), "PILLAR");

        SiteLocationType siteLocationType = siteLogType.getSiteLocation();
        Assert.assertEquals(Double.parseDouble(siteLocationType.getApproximatePositionITRF().getXCoordinateInMeters()),
                Double.parseDouble("-4052051.7670"));
        Assert.assertEquals("AUS", siteLocationType.getCountryCodeISO());
        Assert.assertEquals(siteLocationType.getCity(), "Alice Springs");
        Assert.assertEquals(siteLocationType.getState(), "Northern Territory");
        Assert.assertEquals(siteLocationType.getTectonicPlate().getValue(), "Australian");
        Assert.assertEquals(siteLocationType.getApproximatePositionITRF().getXCoordinateInMeters(), "-4052051.767");
        Assert.assertEquals(siteLocationType.getApproximatePositionITRF().getYCoordinateInMeters(), "4212836.215");
        Assert.assertEquals(siteLocationType.getApproximatePositionITRF().getZCoordinateInMeters(), "-2545106.027");
        Assert.assertEquals(siteLocationType.getApproximatePositionITRF().getLatitudeNorth().doubleValue(), -23.670124,
                0.00001);
        Assert.assertEquals(siteLocationType.getApproximatePositionITRF().getLongitudeEast().doubleValue(), 133.885513,
                0.00001);
        Assert.assertEquals(siteLocationType.getNotes(), "ARGN (Australian Regional GPS Network)");

        // In SOPAC XML (input) ALIC.xml, the foundationDepth is empty but required
        // (<siteIdentification>..<foundationDepth></foundationDepth>..)
        // Because it is required I want the empty value to come out
        Assert.assertNull(siteIdentificationType.getFoundationDepth());

        // Receivers
        List<GnssReceiverPropertyType> receivers = siteLogType.getGnssReceivers();
        Assert.assertEquals(12, receivers.size());
        // Make sure they all have a dateInstalled and dateRemoved after adding GeodesyMLDozerEventListener_GnssReceiverType
        for (GnssReceiverPropertyType receiver : receivers) {
            Assert.assertNotNull(receiver.getGnssReceiver().getDateInstalled());
            Assert.assertNotNull(receiver.getDateInserted());
            // The translate inserts a DateInstalled and sets same as DateInserted
            Assert.assertEquals(receiver.getDateInserted().getValue().get(0),receiver.getGnssReceiver().getDateInstalled().getValue().get(0));
            Assert.assertNotNull(receiver.getGnssReceiver().getDateRemoved());
        }

        Collections.sort(receivers, (r1, r2) -> r1.getGnssReceiver().getFirmwareVersion()
                .compareTo(r2.getGnssReceiver().getFirmwareVersion()));
        GnssReceiverType receiver1 = receivers.get(0).getGnssReceiver();
        // Test some required fields
        Assert.assertEquals(receiver1.getReceiverType().getValue(), "AOA ICS-4000Z");
        Assert.assertEquals(receiver1.getSatelliteSystem().get(0).getValue(), "GPS");
        Assert.assertEquals(receiver1.getSerialNumber(), "C128");
        Assert.assertEquals(receiver1.getFirmwareVersion(), "3.2.32.9");
        Assert.assertEquals(receiver1.getElevationCutoffSetting(), 0, 0.01);
        Assert.assertEquals(receiver1.getTemperatureStabilization(), 0, 0.01);
        MatcherAssert.assertThat("receiver1.getDateInstalled()", receiver1.getDateInstalled().getValue().get(0),
                Matchers.startsWith("1999-07-31"));
        MatcherAssert.assertThat("receiver1.getDateRemoved()", receiver1.getDateRemoved().getValue().get(0),
                Matchers.startsWith("2000-01-14"));
        Assert.assertEquals(receiver1.getNotes(), "Upgrade of firmware to avoid GPS week");

        // Antennas
        List<GnssAntennaPropertyType> antennas = siteLogType.getGnssAntennas();
        Assert.assertEquals(antennas.size(), 3);
        Assert.assertNotNull(antennas.get(0).getGnssAntenna().getAntennaRadomeType().getValue());
        Assert.assertNotNull(antennas.get(0).getGnssAntenna().getAntennaRadomeType().getCodeSpace());

        Collections.sort(antennas, (a1, a2) -> a1.getGnssAntenna().getAntennaType().getValue()
                .compareTo(a2.getGnssAntenna().getAntennaType().getValue()));
        GnssAntennaType antenna1 = antennas.get(0).getGnssAntenna();
        // Test some required fields
        Assert.assertEquals(antenna1.getAntennaType().getValue(), "AOAD/M_T AUST");
        Assert.assertEquals(antenna1.getSerialNumber(), "318");
        Assert.assertEquals(antenna1.getAntennaReferencePoint().getValue(), "BPA");
        Assert.assertEquals(antenna1.getMarkerArpUpEcc(), 0.007, 0.00001);
        Assert.assertEquals(antenna1.getMarkerArpNorthEcc(), 0.0, 0.01);
        Assert.assertEquals(antenna1.getMarkerArpEastEcc(), 0.0, 0.01);
        Assert.assertEquals(antenna1.getAlignmentFromTrueNorth(), 0.0, 0.01);
        Assert.assertEquals(antenna1.getAntennaRadomeType().getValue(), "AUST");
        MatcherAssert.assertThat("antenna1.getDateInstalled()", antenna1.getDateInstalled().getValue().get(0),
                Matchers.startsWith("1994-05-15"));
        MatcherAssert.assertThat("antenna1.getDateRemoved()", antenna1.getDateRemoved().getValue().get(0),
                Matchers.startsWith("2003-06-15"));
        Assert.assertEquals(antenna1.getNotes(), "Radome was damaged at an unknown time during this period.");

        // SurveyedLocalTiesPropertyType
        List<SurveyedLocalTiesPropertyType> surveyedLocalTies = siteLogType.getSurveyedLocalTies();
        Assert.assertEquals(surveyedLocalTies.size(), 3);

        Collections.sort(surveyedLocalTies, (s1, s2) -> s1.getSurveyedLocalTies().getTiedMarkerName()
                .compareTo(s2.getSurveyedLocalTies().getTiedMarkerName()));
        SurveyedLocalTiesType surveyedTies1 = surveyedLocalTies.get(0).getSurveyedLocalTies();
        // Test some required fields
        Assert.assertEquals(surveyedTies1.getTiedMarkerName(), "RM1");
        Assert.assertEquals(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDx(), -15.366,
                0.0001);
        Assert.assertEquals(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDy(), -9.632,
                0.0001);
        Assert.assertEquals(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDz(), 9.09, 0.01);
        Assert.assertEquals(surveyedTies1.getLocalSiteTiesAccuracy(), 1.0, 0.01);
        MatcherAssert.assertThat("surveyedTies1.getDateMeasured()", surveyedTies1.getDateMeasured().getValue().get(0),
                Matchers.startsWith("1992-08-11"));

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
        Assert.assertEquals(humiditySensor.getHeightDiffToAntenna(), 2.4, 0.001);
        Assert.assertEquals(humiditySensor.getDataSamplingInterval(), 30, 0.01);
        Assert.assertEquals(humiditySensor.getAccuracyPercentRelativeHumidity(), 2.0, 0.001);
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
        Assert.assertEquals(pressureSensor.getHeightDiffToAntenna(), 2.4, 0.01);
        Assert.assertEquals(pressureSensor.getDataSamplingInterval(), 30, 0.01);
        Assert.assertEquals(pressureSensor.getAccuracyHPa(), 0.1, 0.001);

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
        Assert.assertEquals(temperatureSensor.getHeightDiffToAntenna(), 2.4, 0.001);
        Assert.assertEquals(temperatureSensor.getDataSamplingInterval().doubleValue(), 30, 0.001);
        Assert.assertEquals(temperatureSensor.getAccuracyDegreesCelcius().doubleValue(), 0.5, 0.001);
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

        // Contacts
        CIResponsiblePartyType siteContact = siteLogType.getSiteContact().get(0).getCIResponsibleParty();
        Assert.assertEquals(siteContact.getOrganisationName().getCharacterString().getValue(), "Geoscience Australia");
        Assert.assertEquals(siteContact.getIndividualName().getCharacterString().getValue(), "Ryan Ruddick");
        Assert.assertEquals(siteContact.getContactInfo().getCIContact().getAddress().getCIAddress()
                .getElectronicMailAddress().get(0).getCharacterString().getValue(), "ryan.ruddick@ga.gov.au");
        Assert.assertEquals(siteContact.getContactInfo().getCIContact().getPhone().getCITelephone().getVoice().get(0)
                .getCharacterString().getValue(), "+61 2 6249 9426");
        Assert.assertEquals(siteContact.getContactInfo().getCIContact().getPhone().getCITelephone().getFacsimile()
                .get(0).getCharacterString().getValue(), "+61 2 6249 9929");

        CIResponsiblePartyType siteMetadataCustodian = siteLogType.getSiteMetadataCustodian().getCIResponsibleParty();
        Assert.assertEquals(siteMetadataCustodian.getOrganisationName().getCharacterString().getValue(),
                "Geoscience Australia 2");
        Assert.assertEquals(siteMetadataCustodian.getIndividualName().getCharacterString().getValue(), "Bob Twilley");
        Assert.assertEquals(siteMetadataCustodian.getContactInfo().getCIContact().getAddress().getCIAddress()
                .getElectronicMailAddress().get(0).getCharacterString().getValue(), "bob.twilley@ga.gov.au");
        Assert.assertEquals(siteMetadataCustodian.getContactInfo().getCIContact().getPhone().getCITelephone().getVoice()
                .get(0).getCharacterString().getValue(), "+61 2 6249 9066");
        Assert.assertEquals(siteMetadataCustodian.getContactInfo().getCIContact().getPhone().getCITelephone()
                .getFacsimile().get(0).getCharacterString().getValue(), "+61 2 6249 9929");

        // More Information
        MoreInformationType moreInformationType = siteLogType.getMoreInformation();
        Assert.assertEquals(moreInformationType.getSiteDiagram(), "Y");
        Assert.assertEquals(moreInformationType.getHorizonMask(), "Y");
        Assert.assertEquals(moreInformationType.getSitePictures(), "Y");
    }

    @Test
    public void testARTU() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "ARTU");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void test00NA() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "00NA");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void testMAT1() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TESTDATADIR, "MAT1");

        Assert.assertNotNull(geodesyML);

        SiteLogType siteLogType = getSiteLog(geodesyML);

        // test the new elements (compared to ALIC)

        List<CollocationInformationPropertyType> collocations = siteLogType.getCollocationInformations();
        Assert.assertNotNull(collocations);
        Assert.assertEquals(collocations.size(), 3);

        // RadioInterferencesPropertyType
        List<RadioInterferencesPropertyType> radioInterferences = siteLogType.getRadioInterferencesSet();
        Assert.assertNotNull(radioInterferences);
        Assert.assertEquals(radioInterferences.size(), 1);

        RadioInterferencesType radioInterference = radioInterferences.get(0).getRadioInterferences();
        Assert.assertEquals(radioInterference.getPossibleProblemSources(), "TV");
        Assert.assertEquals(radioInterference.getNotes(), "multiple lines 1");
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(radioInterference.getValidTime()).getBeginPosition().getValue().get(0)),
                "31 Mar 2015 00:00 GMT");

        // MultipathSourcesPropertyType
        List<MultipathSourcesPropertyType> multipathSources = siteLogType.getMultipathSourcesSet();
        Assert.assertNotNull(multipathSources);
        Assert.assertEquals(multipathSources.size(), 1);

        BasePossibleProblemSourcesType multipathSource = multipathSources.get(0).getMultipathSources();
        Assert.assertEquals(multipathSource.getPossibleProblemSources(), "VIDEO");
        Assert.assertEquals(multipathSource.getNotes(), "multiple lines 2");
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(multipathSource.getValidTime()).getBeginPosition().getValue().get(0)),
                "30 Mar 2015 00:00 GMT");

        // SignalObstructionsPropertyType
        List<SignalObstructionsPropertyType> signalObstructions = siteLogType.getSignalObstructionsSet();
        Assert.assertNotNull(signalObstructions);
        Assert.assertEquals(signalObstructions.size(), 1);

        BasePossibleProblemSourcesType signalObstruction = signalObstructions.get(0).getSignalObstructions();
        Assert.assertEquals(signalObstruction.getPossibleProblemSources(), "TRACTOR");
        Assert.assertEquals(signalObstruction.getNotes(), "multiple lines 3");
        Assert.assertEquals(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(signalObstruction.getValidTime()).getBeginPosition().getValue().get(0)),
                "29 Mar 2015 00:00 GMT");

        // Contacts - just different and new parts (compared to ALIC)
        CIResponsiblePartyType siteContact = siteLogType.getSiteContact().get(0).getCIResponsibleParty();
        Assert.assertEquals(siteContact.getContactInfo().getCIContact().getAddress().getCIAddress().getDeliveryPoint()
                .get(0).getCharacterString().getValue(), "P.O.BOX OPEN - 75100 Matera, ITALY");

        CIResponsiblePartyType siteMetaDataCustodian = siteLogType.getSiteMetadataCustodian().getCIResponsibleParty();
        Assert.assertEquals(siteMetaDataCustodian.getContactInfo().getCIContact().getAddress().getCIAddress()
                .getDeliveryPoint().get(0).getCharacterString().getValue(), "P.O.BOX OPEN - 75100 Matera, ITALY");
    }

    @Test
    public void testZIMJ() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "ZIMJ");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void testBHIL() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "BHIL");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void testALBH() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "ALBH");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void testALGO() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "ALGO");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void testAMC2() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "AMC2");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void testCOOB() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "COOB");

        Assert.assertNotNull(geodesyML);
    }

    @Test
    public void testDEAR() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(SITEDATADIR, "DEAR");

        Assert.assertNotNull(geodesyML);
    }
}
