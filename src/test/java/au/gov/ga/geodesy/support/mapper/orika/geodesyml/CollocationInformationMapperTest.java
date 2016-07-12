package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("AIRA-collocationInfo"), GeodesyMLType.class)
            .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
            .findFirst().get();

        CollocationInformationType collocationInfoTypeA = siteLog.getCollocationInformations().get(0).getCollocationInformation();
        CollocationInformation collocationInfo = mapper.to(collocationInfoTypeA);

        assertThat(collocationInfo.getInstrumentType(), is(collocationInfoTypeA.getInstrumentationType().getValue()));
        assertThat(collocationInfo.getStatus(), is(String.valueOf(collocationInfoTypeA.getStatus().getValue())));
        assertThat(collocationInfo.getNotes(), is(collocationInfoTypeA.getNotes()));
        assertThat(outputFormat.format(collocationInfo.getEffectiveDates().getFrom()),
                is(GMLDateUtils.stringToDateToStringMultiParsers(
                        ((TimePeriodType) collocationInfoTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                                .getBeginPosition().getValue().get(0))));

        CollocationInformationType collocationInfoTypeB = mapper.from(collocationInfo);

        assertThat(collocationInfoTypeB.getInstrumentationType().getValue(), is(collocationInfo.getInstrumentType()));
        assertThat(collocationInfoTypeB.getStatus().getValue(), is(collocationInfo.getStatus()));
        assertThat(collocationInfoTypeB.getNotes(), is(collocationInfo.getNotes()));
        assertThat(GMLDateUtils.stringToDateToStringMultiParsers(
                ((TimePeriodType) collocationInfoTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                    .getBeginPosition().getValue().get(0)),
                is(outputFormat.format(collocationInfo.getEffectiveDates().getFrom())));
    }
}
