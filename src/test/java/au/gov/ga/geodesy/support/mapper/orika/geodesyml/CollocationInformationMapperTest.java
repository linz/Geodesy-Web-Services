package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import au.gov.ga.geodesy.domain.model.sitelog.CollocationInformation;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.CollocationInformationType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import org.testng.annotations.Test;

/**
 * Tests the mapping of a GeodesyML CollocationInformation element
 * to and from a CollocationInformation domain object.
 */
public class CollocationInformationMapperTest {

    private CollocationInformationMapper mapper = new CollocationInformationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from CollocationInformationType to CollocationInformation and back
     * to CollocationInformationType.
     **/
    @Test
    public void testMapping() throws Exception {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").withZone(ZoneId.of("UTC"));
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-moreInfo"), GeodesyMLType.class)
            .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
            .findFirst().get();

        CollocationInformationType collocationInfoTypeA = siteLog.getCollocationInformations().get(0).getCollocationInformation();
        CollocationInformation collocationInfo = mapper.to(collocationInfoTypeA);

        assertEquals(collocationInfo.getInstrumentType(), collocationInfoTypeA.getInstrumentationType().getValue());
        assertEquals(collocationInfo.getStatus(), String.valueOf(collocationInfoTypeA.getStatus().getValue()));
        assertEquals(collocationInfo.getNotes(), collocationInfoTypeA.getNotes());
        assertEquals(outputFormat.format(collocationInfo.getEffectiveDates().getFrom()),
                GMLDateUtils.stringToDateToStringMultiParsers(
                        ((TimePeriodType) collocationInfoTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                                .getBeginPosition().getValue().get(0)));

        CollocationInformationType collocationInfoTypeB = mapper.from(collocationInfo);

        assertEquals(collocationInfoTypeB.getInstrumentationType().getValue(), collocationInfo.getInstrumentType());
        assertEquals(collocationInfoTypeB.getStatus().getValue(), collocationInfo.getStatus());
        assertEquals(collocationInfoTypeB.getNotes(), collocationInfo.getNotes());
        assertEquals(GMLDateUtils.stringToDateToStringMultiParsers(
                ((TimePeriodType) collocationInfoTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                    .getBeginPosition().getValue().get(0)),
                outputFormat.format(collocationInfo.getEffectiveDates().getFrom()));
    }
}
