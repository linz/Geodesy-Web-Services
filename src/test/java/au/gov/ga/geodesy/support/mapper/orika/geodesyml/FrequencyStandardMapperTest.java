package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.FrequencyStandardLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

public class FrequencyStandardMapperTest {

    private FrequencyStandardMapper mapper = new FrequencyStandardMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("MOBS"), GeodesyMLType.class)
            .getValue();

        SiteLogType siteLog =
            GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
            .findFirst()
            .get();

        FrequencyStandardType frequencyStandardTypeA = siteLog.getFrequencyStandards().get(0).getFrequencyStandard();
        FrequencyStandardLogItem logItem = mapper.to(frequencyStandardTypeA);

        assertEquals(logItem.getType(), frequencyStandardTypeA.getStandardType().getValue());
        assertEquals(logItem.getInputFrequency(), String.valueOf(frequencyStandardTypeA.getInputFrequency()));
        assertEquals(logItem.getNotes(), frequencyStandardTypeA.getNotes());

        assertEquals(logItem.getEffectiveDates().getFrom(), GMLDateUtils.stringToDate(
                ((TimePeriodType) frequencyStandardTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0), "uuuu-MM-ddX")
            );

        FrequencyStandardType frequencyStandardTypeB = mapper.from(logItem);

        assertEquals(frequencyStandardTypeB.getStandardType().getValue(), logItem.getType());
        assertEquals(frequencyStandardTypeB.getStandardType().getCodeSpace(), "eGeodesy/frequencyStandardType");
        assertEquals(frequencyStandardTypeB.getNotes(), logItem.getNotes());

        assertEquals(GMLDateUtils.stringToDate(
                ((TimePeriodType) frequencyStandardTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0), "uuuu-MM-dd'T'HH:mm:ss.SSSX"), logItem.getEffectiveDates().getFrom());
    }

}
