package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.HumiditySensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.LogItem;
import au.gov.ga.geodesy.domain.model.sitelog.OtherInstrumentationLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.PressureSensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.SignalObstructionLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.model.sitelog.TemperatureSensorLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.WaterVaporSensorLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.gml.GMLPropertyType;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.BasePossibleProblemSourcesType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.OtherInstrumentationPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.OtherInstrumentationType;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.SignalObstructionsPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.WaterVaporSensorPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.WaterVaporSensorType;
import ma.glasnost.orika.metadata.TypeFactory;
import net.opengis.gml.v_3_2_1.TimePositionType;

public class SiteLogMapperTest {

    private SiteLogMapper mapper = new SiteLogMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from SiteLogType to SiteLog and back
     * to SiteLogType.
     **/
    @Test
    public void testMapping() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        SiteLog siteLog = mapper.to(siteLogType);
        testMappingValues(siteLogType, siteLog);

        // TODO: test the from mapping when it is implemented
        // SiteLogType mappedSiteLogType = mapper.from(siteLog);
        // testMappingValues(mappedSiteLogType, siteLog);

    }

    /**
     * Test mapping from SiteLogType to SiteLog and back
     * to SiteLogType. Based on the MOBS site with added sensors.
     **/
    @Test
    public void testSensorsMapping() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-sensors"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        SiteLog siteLog = mapper.to(siteLogType);
        testMappingValues(siteLogType, siteLog);

        List<HumiditySensorPropertyType> humiditySensors = siteLogType.getHumiditySensors();
        assertEquals(siteLog.getHumiditySensors().size(), 2);
        assertEquals(humiditySensors.size(), 2);

        {
            int i = 0;
            for (HumiditySensorLogItem logItem : sortLogItems(siteLog.getHumiditySensors())) {
                HumiditySensorType xmlType = humiditySensors.get(i++).getHumiditySensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        List<PressureSensorPropertyType> pressureSensors = siteLogType.getPressureSensors();
        assertEquals(siteLog.getPressureSensors().size(), 2);
        assertEquals(pressureSensors.size(), 2);

        {
            int i = 0;
            for (PressureSensorLogItem logItem : sortLogItems(siteLog.getPressureSensors())) {
                PressureSensorType xmlType = pressureSensors.get(i++).getPressureSensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        List<TemperatureSensorPropertyType> temperatureSensors = siteLogType.getTemperatureSensors();
        assertEquals(siteLog.getTemperatureSensors().size(), 2);
        assertEquals(temperatureSensors.size(), 2);

        {
            int i = 0;
            for (TemperatureSensorLogItem logItem : sortLogItems(siteLog.getTemperatureSensors())) {
                TemperatureSensorType xmlType = temperatureSensors.get(i++).getTemperatureSensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        List<WaterVaporSensorPropertyType> waterVaporSensors = siteLogType.getWaterVaporSensors();
        assertEquals(siteLog.getWaterVaporSensors().size(), 2);
        assertEquals(waterVaporSensors.size(), 2);

        {
            int i = 0;
            for (WaterVaporSensorLogItem logItem : sortLogItems(siteLog.getWaterVaporSensors())) {
                WaterVaporSensorType xmlType = waterVaporSensors.get(i++).getWaterVaporSensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        // TODO: test the from mapping when it is implemented
        // SiteLogType mappedSiteLogType = mapper.from(siteLog);
        // testMappingValues(mappedSiteLogType, siteLog);

    }

    /**
     * Test mapping from SiteLogType to SiteLog and back
     * to SiteLogType. Based on the QIKI site with added sensors.
     **/
    @Test
    public void testOtherInstrumentationsMapping() throws Exception {
        GeodesyMLType mobs = marshaller
                .unmarshal(TestResources.geodesyMLTestDataSiteLogReader("QIKI-otherInstrumentation"),
                        GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        SiteLog siteLog = mapper.to(siteLogType);

        List<OtherInstrumentationPropertyType> otherInstrumentationPropertyTypes = siteLogType.getOtherInstrumentations();
        sortGMLPropertyTypes(otherInstrumentationPropertyTypes);

        assertEquals(siteLogType.getOtherInstrumentations().size(), 3);
        assertEquals(otherInstrumentationPropertyTypes.size(), 3);

        {
            int i = 0;
            for (OtherInstrumentationLogItem logItem : sortLogItems(siteLog.getOtherInstrumentationLogItem())) {
                OtherInstrumentationType xmlType = otherInstrumentationPropertyTypes.get(i++).getOtherInstrumentation();
                assertEquals(logItem.getInstrumentation(), xmlType.getInstrumentation());
            }
        }
    }

    /**
     * Test mapping from SiteLogType to SiteLog and back
     * to SiteLogType. Based on the METZ site log with added signal obstructions.
     **/
    @Test
    public void testSignalObstructionsMapping() throws Exception {
        GeodesyMLType mobs = marshaller
                .unmarshal(TestResources.geodesyMLTestDataSiteLogReader("METZ-signalObstructionSet"),
                        GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        SiteLog siteLog = mapper.to(siteLogType);

        List<SignalObstructionsPropertyType> signalObstructionsPropertyTypes = siteLogType.getSignalObstructionsSet();
        sortGMLPropertyTypes(signalObstructionsPropertyTypes);

        assertEquals(siteLogType.getSignalObstructionsSet().size(), 2);
        assertEquals(signalObstructionsPropertyTypes.size(), 2);

        {
            int i = 0;
            for (SignalObstructionLogItem logItem : sortLogItems(siteLog.getSignalObstructionLogItems())) {
                BasePossibleProblemSourcesType xmlType = signalObstructionsPropertyTypes.get(i++).getSignalObstructions();
                assertEquals(logItem.getPossibleProblemSource(), xmlType.getPossibleProblemSources());
            }
        }
    }

    private void testMappingValues(SiteLogType siteLogType, SiteLog siteLog) {
        assertEquals(siteLog.getSiteIdentification().getSiteName(), siteLogType.getSiteIdentification().getSiteName());
        assertEquals(siteLog.getSiteLocation().getTectonicPlate(), siteLogType.getSiteLocation().getTectonicPlate().getValue());

        List<GnssReceiverPropertyType> receiverProperties = siteLogType.getGnssReceivers();
        sortGMLPropertyTypes(receiverProperties);
        assertEquals(siteLog.getGnssReceivers().size(), 9);
        assertEquals(receiverProperties.size(), 9);

        {
            int i = 0;
            for (GnssReceiverLogItem receiverLogItem : sortLogItems(siteLog.getGnssReceivers())) {
                GnssReceiverType receiverType = receiverProperties.get(i++).getGnssReceiver();
                assertEquals(receiverLogItem.getFirmwareVersion(), receiverType.getFirmwareVersion());
            }
        }
    }

    /**
     * Sort set of log items by installation date.
     */
    private <T extends LogItem> SortedSet<T> sortLogItems(Set<T> logItems) {
        SortedSet<T> sorted = new TreeSet<>(new Comparator<T>() {
            public int compare(T e, T f) {
                int c = e.getEffectiveDates().compareTo(f.getEffectiveDates());
                // keep duplicates
                return c != 0 ? c : 1;
            }
        });
        sorted.addAll(logItems);
        return sorted;
    }

    /**
     * Sort list of GMLPropertyType objects by installation date.
     */
    private <P extends GMLPropertyType> void sortGMLPropertyTypes(List<P> list) {
        Collections.sort(list, new Comparator<P>() {
            public int compare(P p, P q) {
                return dateInstalled(p).compareTo(dateInstalled(q));
            }

            private Instant dateInstalled(P p) {

                TimePositionType time = null;

                try {
                    time = (TimePositionType) PropertyUtils.getProperty(p.getTargetElement(),"dateInstalled");
                    return new InstantToTimePositionConverter().convertFrom(time, TypeFactory.valueOf(Instant.class), null);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    // try a different version of the "installation date"
                    try {
                        time = (TimePositionType)PropertyUtils.getProperty(p.getTargetElement(),
                                "validTime.abstractTimePrimitive.value.beginPosition");
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e2) {
                        throw new RuntimeException(e2);
                    }
                }
                return new InstantToTimePositionConverter().convertFrom(time, TypeFactory.valueOf(Instant.class), null);
            }
        });
    }


}

