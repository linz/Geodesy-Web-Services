package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.EquipmentLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.gml.GMLPropertyType;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;

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
        try (Reader mobs = new FileReader(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS.xml"))) {
            GeodesyMLType geodesyML = marshaller.unmarshal(mobs, GeodesyMLType.class).getValue();
            SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(geodesyML.getElements(), SiteLogType.class)
                .findFirst()
                .get();

            SiteLog siteLog = mapper.to(siteLogType);
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
        // TODO: complete test
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

