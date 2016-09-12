package au.gov.ga.geodesy.support.mapper.dozer;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.port.adapter.geodesyml.MarshallingException;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
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
import net.opengis.gml.v_3_2_1.AbstractTimePrimitiveType;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import net.opengis.gml.v_3_2_1.TimePositionType;
import net.opengis.gml.v_3_2_1.TimePrimitivePropertyType;
import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBElement;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class TranslateTest { // extends AbstractTestNGSpringContextTests {

    private IgsSiteLogXmlMarshaller marshaller;

    private GeodesyMLSiteLogDozerTranslator geodesyMLSiteLogTranslator;

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
     * @param inputResourceDir - directory in the resources in which the input file exists
     * @param inputFile        - file to test
     * @return
     * @throws MarshallingException
     * @throws IOException
     * @throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException
     * @throws ParseException
     */
    public GeodesyMLType testTranslate(String inputResourceDir, String inputFile) throws MarshallingException,
            IOException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        String source = inputResourceDir.substring(1) + inputFile + ".xml";


        System.out.println(source);

        String destTmpName = inputFile + ".out.xml";

        Reader input = new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(source));

        IgsSiteLog siteLog = marshaller.unmarshal(input);

        JAXBElement<GeodesyMLType> geodesyMLJAXB = geodesyMLSiteLogTranslator.dozerTranslate(siteLog);

        GeodesyMLType geodesyML = geodesyMLJAXB.getValue();

        assertThat(geodesyML, notNullValue());

        System.out.println("Write xml file to: " + returnTestFile(destTmpName).toString());
        FileWriter writer = new FileWriter(returnTestFile(destTmpName).toFile());
        geodesyMLMarshaller.marshal(geodesyMLJAXB, writer);

        return geodesyML;
    }

    private SiteLogType getSiteLog(GeodesyMLType geodesyML) {
        assertThat(geodesyML.getElements().size(), equalTo(1));
        assertThat(geodesyML.getElements().get(0), instanceOf(JAXBElement.class)); // TODO: remove this check

        Stream<SiteLogType> siteLogTypeStream = GeodesyMLUtils.getElementFromJAXBElements(
                geodesyML.getElements(), SiteLogType.class);

        SiteLogType siteLogType = siteLogTypeStream.collect(Collectors.toList()).get(0);
        return siteLogType;
    }

    @Test
    public void testALIC() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "ALIC");

        SiteLogType siteLogType = getSiteLog(geodesyML);

        FormInformationType formIdentificationType = siteLogType.getFormInformation();
        assertThat(formIdentificationType.getPreparedBy(), equalTo("Nick Dando"));

        assertThat("formIdentificationType.getDatePrepared()",
                formIdentificationType.getDatePrepared().getValue().get(0), startsWith("2013-07-08"));

        assertThat(formIdentificationType.getReportType(), equalTo("UPDATE"));
        // These don't exist (should they - Mmm, not in Sopac SiteLog either so no)?
        // Assert.assertEquals(formIdentificationType.getPreviousLog(), "alic_20130308.log");
        // Assert.assertEquals(formIdentificationType.getModifiedSections(), "3.11, 3.12");

        // <mi:previousLog>alic_20130308.log</mi:previousLog>
        // <mi:modifiedSections>3.11, 3.12</mi:modifiedSections>

        SiteIdentificationType siteIdentificationType = siteLogType.getSiteIdentification();
        assertThat(siteIdentificationType.getMonumentDescription().getValue(), equalTo("PILLAR"));

        SiteLocationType siteLocationType = siteLogType.getSiteLocation();
        assertThat(Double.parseDouble(siteLocationType.getApproximatePositionITRF().getXCoordinateInMeters()),
                closeTo(Double.parseDouble("-4052051.7670"), 0.1));
        assertThat(siteLocationType.getCountryCodeISO(), equalTo("AUS"));
        assertThat(siteLocationType.getCity(), equalTo("Alice Springs"));
        assertThat(siteLocationType.getState(), equalTo("Northern Territory"));
        assertThat(siteLocationType.getTectonicPlate().getValue(), equalTo("Australian"));
        assertThat(siteLocationType.getApproximatePositionITRF().getXCoordinateInMeters(), equalTo("-4052051.767"));
        assertThat(siteLocationType.getApproximatePositionITRF().getYCoordinateInMeters(), equalTo("4212836.215"));
        assertThat(siteLocationType.getApproximatePositionITRF().getZCoordinateInMeters(), equalTo("-2545106.027"));
        assertThat(siteLocationType.getApproximatePositionITRF().getLatitudeNorth().doubleValue(), closeTo(-23.670124,
                0.00001));
        assertThat(siteLocationType.getApproximatePositionITRF().getLongitudeEast().doubleValue(), closeTo(133.885513,
                0.00001));
        assertThat(siteLocationType.getNotes(), equalTo("ARGN (Australian Regional GPS Network)"));

        // In SOPAC XML (input) ALIC.xml, the foundationDepth is empty but required
        // (<siteIdentification>..<foundationDepth></foundationDepth>..)
        // Because it is required I want the empty value to come out
        assertThat(siteIdentificationType.getFoundationDepth(), nullValue());

        // Receivers
        List<GnssReceiverPropertyType> receivers = siteLogType.getGnssReceivers();
        assertThat(receivers.size(), equalTo(12));
        // Make sure they all have a dateInstalled and dateRemoved after adding GeodesyMLDozerEventListener_GnssReceiverType
        for (GnssReceiverPropertyType receiver : receivers) {
            assertThat(receiver.getGnssReceiver().getDateInstalled(),notNullValue());
            assertThat(receiver.getDateInserted(),notNullValue());
            // The translate inserts a DateInstalled and sets same as DateInserted
            assertThat(receiver.getDateInserted().getValue().get(0),
                    equalTo(receiver.getGnssReceiver().getDateInstalled().getValue().get(0)));
            assertThat(receiver.getGnssReceiver().getDateRemoved(), notNullValue());
        }

        Collections.sort(receivers, (r1, r2) -> r1.getGnssReceiver().getFirmwareVersion()
                .compareTo(r2.getGnssReceiver().getFirmwareVersion()));
        GnssReceiverType receiver1 = receivers.get(0).getGnssReceiver();
        // Test some required fields
        assertThat(receiver1.getReceiverType().getValue(), equalTo("AOA ICS-4000Z"));
        assertThat(receiver1.getSatelliteSystem().get(0).getValue(), equalTo("GPS"));
        assertThat(receiver1.getSerialNumber(), equalTo("C128"));
        assertThat(receiver1.getFirmwareVersion(), equalTo("3.2.32.9"));
        assertThat(receiver1.getElevationCutoffSetting(), closeTo(0, 0.01));
        assertThat(receiver1.getTemperatureStabilization(), closeTo(0, 0.01));
        assertThat("receiver1.getDateInstalled()", receiver1.getDateInstalled().getValue().get(0),
                startsWith("1999-07-31"));
        assertThat("receiver1.getDateRemoved()", receiver1.getDateRemoved().getValue().get(0),
                startsWith("2000-01-14"));
        assertThat(receiver1.getNotes(), equalTo("Upgrade of firmware to avoid GPS week"));

        // Antennas
        List<GnssAntennaPropertyType> antennas = siteLogType.getGnssAntennas();
        assertThat(antennas.size(), equalTo(3));
        assertThat(antennas.get(0).getGnssAntenna().getAntennaRadomeType().getValue(), notNullValue());
        assertThat(antennas.get(0).getGnssAntenna().getAntennaRadomeType().getCodeSpace(), notNullValue());

        Collections.sort(antennas, (a1, a2) -> a1.getGnssAntenna().getAntennaType().getValue()
                .compareTo(a2.getGnssAntenna().getAntennaType().getValue()));
        GnssAntennaType antenna1 = antennas.get(0).getGnssAntenna();
        // Test some required fields
        assertThat(antenna1.getAntennaType().getValue(), equalTo("AOAD/M_T AUST"));
        assertThat(antenna1.getSerialNumber(), equalTo("318"));
        assertThat(antenna1.getAntennaReferencePoint().getValue(), equalTo("BPA"));
        assertThat(antenna1.getMarkerArpUpEcc(), closeTo(0.007, 0.00001));
        assertThat(antenna1.getMarkerArpNorthEcc(), closeTo(0.0, 0.01));
        assertThat(antenna1.getMarkerArpEastEcc(), closeTo(0.0, 0.01));
        assertThat(antenna1.getAlignmentFromTrueNorth(), closeTo(0.0, 0.01));
        assertThat(antenna1.getAntennaRadomeType().getValue(), equalTo("AUST"));
        assertThat("antenna1.getDateInstalled()", antenna1.getDateInstalled().getValue().get(0),
                startsWith("1994-05-15"));
        assertThat("antenna1.getDateRemoved()", antenna1.getDateRemoved().getValue().get(0),
                startsWith("2003-06-15"));
        assertThat(antenna1.getNotes(), equalTo("Radome was damaged at an unknown time during this period."));

        // SurveyedLocalTiesPropertyType
        List<SurveyedLocalTiesPropertyType> surveyedLocalTies = siteLogType.getSurveyedLocalTies();
        assertThat(surveyedLocalTies.size(), equalTo(3));

        Collections.sort(surveyedLocalTies, (s1, s2) -> s1.getSurveyedLocalTies().getTiedMarkerName()
                .compareTo(s2.getSurveyedLocalTies().getTiedMarkerName()));
        SurveyedLocalTiesType surveyedTies1 = surveyedLocalTies.get(0).getSurveyedLocalTies();
        // Test some required fields
        assertThat(surveyedTies1.getTiedMarkerName(), equalTo("RM1"));
        assertThat(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDx(), closeTo(-15.366,
                0.0001));
        assertThat(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDy(), closeTo(-9.632,
                0.0001));
        assertThat(surveyedTies1.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDz(), closeTo(9.09, 0.01));
        assertThat(surveyedTies1.getLocalSiteTiesAccuracy(), closeTo(1.0, 0.01));
        assertThat("surveyedTies1.getDateMeasured()", surveyedTies1.getDateMeasured().getValue().get(0),
                startsWith("1992-08-12"));

        // FrequencyStandardPropertyType
        List<FrequencyStandardPropertyType> frequencyStandards = siteLogType.getFrequencyStandards();
        assertThat(frequencyStandards.size(), equalTo(1));

        FrequencyStandardType frequencyStandardType = frequencyStandards.get(0).getFrequencyStandard();
        // Test some required fields
        assertThat(frequencyStandardType.getStandardType().getValue(), equalTo("QUARTZ/INTERNAL"));
        assertThat(GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(frequencyStandardType.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("1994-05-15T00:00:00.000Z"));

        // Humidity Sensors
        List<HumiditySensorPropertyType> humiditySensors = siteLogType.getHumiditySensors();
        assertThat(humiditySensors.size(), equalTo(1));

        HumiditySensorType humiditySensor = humiditySensors.get(0).getHumiditySensor();
        // Test some required fields
        assertThat(humiditySensor.getType().getValue(), equalTo("PAROSCIENTIFIC MET3A"));
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(humiditySensor.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("2006-03-29T00:00:00.000Z"));
        assertThat(humiditySensor.getSerialNumber(), equalTo("98850"));
        assertThat(humiditySensor.getHeightDiffToAntenna(), closeTo(2.4, 0.001));
        assertThat(humiditySensor.getDataSamplingInterval(), closeTo(30, 0.01));
        assertThat(humiditySensor.getAccuracyPercentRelativeHumidity(), closeTo(2.0, 0.001));
        assertThat(humiditySensor.getAspiration(), equalTo("FAN"));

        // Pressuer Sensors
        List<PressureSensorPropertyType> pressureSensors = siteLogType.getPressureSensors();
        assertThat(pressureSensors.size(), equalTo(1));

        PressureSensorType pressureSensor = pressureSensors.get(0).getPressureSensor();
        // Test some required fields
        assertThat(pressureSensor.getType().getValue(), equalTo("PAROSCIENTIFIC MET3A"));
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(pressureSensor.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("2006-03-29T00:00:00.000Z"));
        assertThat(pressureSensor.getManufacturer(), equalTo("Paroscientific, Inc."));
        assertThat(pressureSensor.getSerialNumber(), equalTo("98850"));
        assertThat(pressureSensor.getHeightDiffToAntenna(), closeTo(2.4, 0.01));
        assertThat(pressureSensor.getDataSamplingInterval(), closeTo(30, 0.01));
        assertThat(pressureSensor.getAccuracyHPa(), closeTo(0.1, 0.001));

        // Water Vapour Sensors
        List<WaterVaporSensorPropertyType> waterVapourSensors = siteLogType.getWaterVaporSensors();
        assertThat(waterVapourSensors.size(), equalTo(1));

        WaterVaporSensorType waterVapourSensor = waterVapourSensors.get(0).getWaterVaporSensor();
        // Test some required fields
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(waterVapourSensor.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("2006-03-29T00:00:00.000Z"));

        // Temperature Sensors
        List<TemperatureSensorPropertyType> temperatureSensors = siteLogType.getTemperatureSensors();
        assertThat(temperatureSensors.size(), equalTo(1));

        TemperatureSensorType temperatureSensor = temperatureSensors.get(0).getTemperatureSensor();
        // Test some required fields
        assertThat(temperatureSensor.getType().getValue(), equalTo("PAROSCIENTIFIC MET3A"));
        assertThat(temperatureSensor.getManufacturer(), equalTo("Paroscientific, Inc."));
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(temperatureSensor.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("2006-03-29T00:00:00.000Z"));
        assertThat(temperatureSensor.getSerialNumber(), equalTo("98850"));
        assertThat(temperatureSensor.getHeightDiffToAntenna(), closeTo(2.4, 0.001));
        assertThat(temperatureSensor.getDataSamplingInterval().doubleValue(), closeTo(30, 0.001));
        assertThat(temperatureSensor.getAccuracyDegreesCelcius().doubleValue(), closeTo(0.5, 0.001));
        assertThat(temperatureSensor.getAspiration(), equalTo("FAN"));

        // Local Episodic Events
        List<LocalEpisodicEventsPropertyType> localEpisodicEvents = siteLogType.getLocalEpisodicEventsSet();
        assertThat(localEpisodicEvents.size(), equalTo(1));

        LocalEpisodicEventsType localEpisodicEvent = localEpisodicEvents.get(0).getLocalEpisodicEvents();
        // Test some required fields
        assertThat(localEpisodicEvent.getEvent(), startsWith("TREE CLEARING"));
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimeInstantType(localEpisodicEvent.getValidTime()).getTimePosition().getValue().get(0)),
            equalTo("2011-07-20T00:00:00.000Z"));

        // Contacts
        CIResponsiblePartyType siteContact = siteLogType.getSiteContact().get(0).getCIResponsibleParty();
        assertThat(siteContact.getOrganisationName().getCharacterString().getValue(), equalTo("Geoscience Australia"));
        assertThat(siteContact.getIndividualName().getCharacterString().getValue(), equalTo("Ryan Ruddick"));
        assertThat(siteContact.getContactInfo().getCIContact().getAddress().getCIAddress()
                .getElectronicMailAddress().get(0).getCharacterString().getValue(), equalTo("ryan.ruddick@ga.gov.au"));
        assertThat(siteContact.getContactInfo().getCIContact().getPhone().getCITelephone().getVoice().get(0)
                .getCharacterString().getValue(), equalTo("+61 2 6249 9426"));
        assertThat(siteContact.getContactInfo().getCIContact().getPhone().getCITelephone().getFacsimile()
                .get(0).getCharacterString().getValue(), equalTo("+61 2 6249 9929"));

        CIResponsiblePartyType siteMetadataCustodian = siteLogType.getSiteMetadataCustodian().getCIResponsibleParty();
        assertThat(siteMetadataCustodian.getOrganisationName().getCharacterString().getValue(),
            equalTo("Geoscience Australia 2"));
        assertThat(siteMetadataCustodian.getIndividualName().getCharacterString().getValue(), equalTo("Bob Twilley"));
        assertThat(siteMetadataCustodian.getContactInfo().getCIContact().getAddress().getCIAddress()
                .getElectronicMailAddress().get(0).getCharacterString().getValue(), equalTo("bob.twilley@ga.gov.au"));
        assertThat(siteMetadataCustodian.getContactInfo().getCIContact().getPhone().getCITelephone().getVoice()
                .get(0).getCharacterString().getValue(), equalTo("+61 2 6249 9066"));
        assertThat(siteMetadataCustodian.getContactInfo().getCIContact().getPhone().getCITelephone()
                .getFacsimile().get(0).getCharacterString().getValue(), equalTo("+61 2 6249 9929"));

        // More Information
        MoreInformationType moreInformationType = siteLogType.getMoreInformation();
        assertThat(moreInformationType.getSiteDiagram(), equalTo("Y"));
        assertThat(moreInformationType.getHorizonMask(), equalTo("Y"));
        assertThat(moreInformationType.getSitePictures(), equalTo("Y"));
    }

    @Test
    public void testARTU() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "ARTU");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void test00NA() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "00NA");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testMAT1() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "MAT1");

        assertThat(geodesyML, notNullValue());

        SiteLogType siteLogType = getSiteLog(geodesyML);

        // test the new elements (compared to ALIC)

        List<CollocationInformationPropertyType> collocations = siteLogType.getCollocationInformations();
        assertThat(collocations, notNullValue());
        assertThat(collocations.size(), equalTo(3));

        // RadioInterferencesPropertyType
        List<RadioInterferencesPropertyType> radioInterferences = siteLogType.getRadioInterferencesSet();
        assertThat(radioInterferences, notNullValue());
        assertThat(radioInterferences.size(), equalTo(1));

        RadioInterferencesType radioInterference = radioInterferences.get(0).getRadioInterferences();
        assertThat(radioInterference.getPossibleProblemSources(), equalTo("TV"));
        assertThat(radioInterference.getNotes(), equalTo("multiple lines 1"));
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(radioInterference.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("2015-03-31T00:00:00.000Z"));

        // MultipathSourcesPropertyType
        List<MultipathSourcesPropertyType> multipathSources = siteLogType.getMultipathSourcesSet();
        assertThat(multipathSources, notNullValue());
        assertThat(multipathSources.size(), equalTo(1));

        BasePossibleProblemSourcesType multipathSource = multipathSources.get(0).getMultipathSources();
        assertThat(multipathSource.getPossibleProblemSources(), equalTo("VIDEO"));
        assertThat(multipathSource.getNotes(), equalTo("multiple lines 2"));
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(multipathSource.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("2015-03-30T00:00:00.000Z"));

        // SignalObstructionsPropertyType
        List<SignalObstructionsPropertyType> signalObstructions = siteLogType.getSignalObstructionsSet();
        assertThat(signalObstructions, notNullValue());
        assertThat(signalObstructions.size(), equalTo(1));

        BasePossibleProblemSourcesType signalObstruction = signalObstructions.get(0).getSignalObstructions();
        assertThat(signalObstruction.getPossibleProblemSources(), equalTo("TRACTOR"));
        assertThat(signalObstruction.getNotes(), equalTo("multiple lines 3"));
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(TimePrimitivePropertyTypeUtils
                        .getTheTimePeriodType(signalObstruction.getValidTime()).getBeginPosition().getValue().get(0)),
            equalTo("2015-03-29T00:00:00.000Z"));

        // Contacts - just different and new parts (compared to ALIC)
        CIResponsiblePartyType siteContact = siteLogType.getSiteContact().get(0).getCIResponsibleParty();
        assertThat(siteContact.getContactInfo().getCIContact().getAddress().getCIAddress().getDeliveryPoint()
                .get(0).getCharacterString().getValue(), equalTo("P.O.BOX OPEN - 75100 Matera, ITALY"));

        CIResponsiblePartyType siteMetaDataCustodian = siteLogType.getSiteMetadataCustodian().getCIResponsibleParty();
        assertThat(siteMetaDataCustodian.getContactInfo().getCIContact().getAddress().getCIAddress()
                .getDeliveryPoint().get(0).getCharacterString().getValue(), equalTo("P.O.BOX OPEN - 75100 Matera, ITALY"));
    }

    @Test
    public void testZIMJ() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ZIMJ");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testBHIL() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "BHIL");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testALBH() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ALBH");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testALGO() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ALGO");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testAMC2() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "AMC2");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testCOOB() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "COOB");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testDEAR() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "DEAR");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testZIMM() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ZIMM");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testZHN1() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ZHN1");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testZECK() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ZECK");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testZAMB() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "ZAMB");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testBALL() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "BALL");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testZABL() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ZABL");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testYSSK() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "YSSK");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testADE1() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ADE1");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testADE2() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ADE2");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testAUCK() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "AUCK");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testBAKO() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "BAKO");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testANTC() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "ANTC");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testBUE2() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "BUE2");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testSELE() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "SELE");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testWOOL() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalSopacSiteLogsDirectory(), "WOOL");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testCRAO() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "CRAO");

        assertThat(geodesyML, notNullValue());
    }

    @Test
    public void testCRO1() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "CRO1");

        assertThat(geodesyML, notNullValue());
    }

    // Tests for EffectiveDates - despite what is in the data (to and/or from dates) there should be both with one or the other being optionally empty
    @Test
    public void testZIMJ_RadioInterference_WithEffectiveDates() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "ZIMJ-radioInterference-withEffectiveDates");

        assertThat(geodesyML, notNullValue());
        SiteLogType siteLogType = getSiteLog(geodesyML);

        List<RadioInterferencesPropertyType> radioInterferences = siteLogType.getRadioInterferencesSet();
        assertThat(radioInterferences, notNullValue());
        assertThat(radioInterferences.size(), equalTo(1));
        RadioInterferencesPropertyType radioInterference = radioInterferences.get(0);

        AbstractTimePrimitiveType validTime = radioInterference.getRadioInterferences().getValidTime().getAbstractTimePrimitive().getValue();
        assertThat(validTime, instanceOf( TimePeriodType.class));

        TimePositionType begin = ((TimePeriodType) validTime).getBeginPosition();
        TimePositionType end = ((TimePeriodType) validTime).getEndPosition();

        assertThat(begin, notNullValue());
        assertThat(begin.getValue().size(), equalTo(1));
        assertThat(end, notNullValue());
        assertThat(end.getValue().size(), equalTo(1));
    }

    @Test
    public void testZIMJ_RadioInterference_WithNoEffectiveDates() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "ZIMJ-radioInterference-noEffectiveDates");

        assertThat(geodesyML, notNullValue());
        SiteLogType siteLogType = getSiteLog(geodesyML);

        List<RadioInterferencesPropertyType> radioInterferences = siteLogType.getRadioInterferencesSet();
        assertThat(radioInterferences, notNullValue());
        assertThat(radioInterferences.size(), equalTo(1));
        RadioInterferencesType radioInterference = radioInterferences.get(0).getRadioInterferences();

        TimePrimitivePropertyType timePrimitive = radioInterference.getValidTime();

        assertThat(timePrimitive, nullValue());
    }


    // EffectiveDates without a To Date should return a TimePeriodType with a getEndPosition() that ISN'T NULL but is an empty list
    public void testZIMJ_RadioInterference_WithEffectiveDates_NoToForms(GeodesyMLType geodesyML) {
        assertThat(geodesyML, notNullValue());
        SiteLogType siteLogType = getSiteLog(geodesyML);

        List<RadioInterferencesPropertyType> radioInterferences = siteLogType.getRadioInterferencesSet();
        assertThat(radioInterferences, notNullValue());
        assertThat(radioInterferences.size(), equalTo(1));
        RadioInterferencesPropertyType radioInterference = radioInterferences.get(0);

        AbstractTimePrimitiveType validTime = radioInterference.getRadioInterferences().getValidTime().getAbstractTimePrimitive().getValue();
        assertThat(validTime, instanceOf(TimePeriodType.class));

        TimePositionType begin = ((TimePeriodType) validTime).getBeginPosition();
        TimePositionType end = ((TimePeriodType) validTime).getEndPosition();

        assertThat(begin, notNullValue());
        assertThat(begin.getValue().size(), equalTo(1));

        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(begin.getValue().get(0)),
            equalTo("2012-01-15T00:00:00.000Z"));

        assertThat(end, nullValue());
    }

    @Test
    public void testZIMJ_RadioInterference_WithEffectiveDates_NoToForm1() throws ParseException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException, IOException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "ZIMJ-radioInterference-withEffectiveDatesNoToForm1");
        testZIMJ_RadioInterference_WithEffectiveDates_NoToForms(geodesyML);
    }

    @Test
    public void testZIMJ_RadioInterference_WithEffectiveDates_NoToForm2() throws ParseException, au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, MarshallingException, IOException {
        GeodesyMLType geodesyML = testTranslate(TestResources.customSopacSiteLogsDirectory(), "ZIMJ-radioInterference-withEffectiveDatesNoToForm2");
        testZIMJ_RadioInterference_WithEffectiveDates_NoToForms(geodesyML);
    }

    // Bad data - want it to fail
    @Test(expectedExceptions = au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException.class)
    public void testBATH() throws MarshallingException, IOException,
            au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException, ParseException {
        GeodesyMLType geodesyML = testTranslate(TestResources.originalBadSopacSiteLogsDirectory(), "BATH");

        assertThat(geodesyML, notNullValue());
    }
}
