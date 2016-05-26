package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import au.gov.ga.geodesy.domain.model.sitelog.*;
import au.gov.ga.geodesy.support.TestResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.gml.GMLPropertyType;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.*;
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
            for (HumiditySensorLogItem logItem : sort(siteLog.getHumiditySensors())) {
                HumiditySensorType xmlType = humiditySensors.get(i++).getHumiditySensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        List<PressureSensorPropertyType> pressureSensors = siteLogType.getPressureSensors();
        assertEquals(siteLog.getPressureSensors().size(), 2);
        assertEquals(pressureSensors.size(), 2);

        {
            int i = 0;
            for (PressureSensorLogItem logItem : sort(siteLog.getPressureSensors())) {
                PressureSensorType xmlType = pressureSensors.get(i++).getPressureSensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        List<TemperatureSensorPropertyType> temperatureSensors = siteLogType.getTemperatureSensors();
        assertEquals(siteLog.getTemperatureSensors().size(), 2);
        assertEquals(temperatureSensors.size(), 2);

        {
            int i = 0;
            for (TemperatureSensorLogItem logItem : sort(siteLog.getTemperatureSensors())) {
                TemperatureSensorType xmlType = temperatureSensors.get(i++).getTemperatureSensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        List<WaterVaporSensorPropertyType> waterVaporSensors = siteLogType.getWaterVaporSensors();
        assertEquals(siteLog.getWaterVaporSensors().size(), 2);
        assertEquals(waterVaporSensors.size(), 2);

        {
            int i = 0;
            for (WaterVaporSensorLogItem logItem : sort(siteLog.getWaterVaporSensors())) {
                WaterVaporSensorType xmlType = waterVaporSensors.get(i++).getWaterVaporSensor();
                assertEquals(logItem.getSerialNumber(), xmlType.getSerialNumber());
            }
        }

        // TODO: test the from mapping when it is implemented
        // SiteLogType mappedSiteLogType = mapper.from(siteLog);
        // testMappingValues(mappedSiteLogType, siteLog);

    }

    private void testMappingValues(SiteLogType siteLogType, SiteLog siteLog) {
        assertEquals(siteLog.getSiteIdentification().getSiteName(), siteLogType.getSiteIdentification().getSiteName());
        assertEquals(siteLog.getSiteLocation().getTectonicPlate(), siteLogType.getSiteLocation().getTectonicPlate().getValue());

        List<GnssReceiverPropertyType> receiverProperties = siteLogType.getGnssReceivers();
        sort(receiverProperties);
        assertEquals(siteLog.getGnssReceivers().size(), 9);
        assertEquals(receiverProperties.size(), 9);

        {
            int i = 0;
            for (GnssReceiverLogItem receiverLogItem : sort(siteLog.getGnssReceivers())) {
                GnssReceiverType receiverType = receiverProperties.get(i++).getGnssReceiver();
                assertEquals(receiverLogItem.getFirmwareVersion(), receiverType.getFirmwareVersion());
            }
        }
    }

    /**
     * Sort set of equipment log items by installation date.
     */
    private <T extends EquipmentLogItem> SortedSet<T> sort(Set<T> logItems) {
        SortedSet<T> sorted = new TreeSet<>(new Comparator<T>() {
            public int compare(T e, T f) {
                int c = e.getEffectiveDates().getFrom().compareTo(f.getEffectiveDates().getFrom());
                // keep duplicates
                return c != 0 ? c : 1;
            }
        });
        sorted.addAll(logItems);
        return sorted;
    }


    /**
     * Sort list of equipment properties by installation date.
     */
    private <P extends GMLPropertyType> void sort(List<P> list) {
        Collections.sort(list, new Comparator<P>() {
            public int compare(P p, P q) {
                return dateInstalled(p).compareTo(dateInstalled(q));
            }

            private Date dateInstalled(P p) {
                try {
                    TimePositionType time = (TimePositionType) PropertyUtils.getProperty(p.getTargetElement(),"dateInstalled");
                    return new DateToTimePositionConverter().convertFrom(time, TypeFactory.valueOf(Date.class), null);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

