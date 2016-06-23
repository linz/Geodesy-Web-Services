package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.LocalEpisodicEventLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.LocalEpisodicEventsType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Tests the mapping of a GeodesyML LocalEpisodicEventsPropertyType element
 * to and from an LocalEpisodicEventLogItem domain object.
 */
public class LocalEpisodicEventMapperTest {

    private LocalEpisodicEventMapper mapper = new LocalEpisodicEventMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLTestDataSiteLogReader("WGTN-localEpisodicEvents"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        LocalEpisodicEventsType localEpisodicEventTypeA =
                siteLog.getLocalEpisodicEventsSet().get(0).getLocalEpisodicEvents();

        LocalEpisodicEventLogItem logItem = mapper.to(localEpisodicEventTypeA);
        assertThat(logItem.getEvent(), equalTo(localEpisodicEventTypeA.getEvent()));
        String xmlEffectiveDateFrom = ((TimePeriodType) localEpisodicEventTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertThat(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
                equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom)));
        String xmlEffectiveDateTo = ((TimePeriodType) localEpisodicEventTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getEndPosition().getValue().get(0);
        assertThat(GMLDateUtils.dateToString(logItem.getEffectiveDates().getTo(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
                equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo)));

        LocalEpisodicEventsType localEpisodicEventTypeB = mapper.from(logItem);
        assertThat(localEpisodicEventTypeB.getEvent(), equalTo(logItem.getEvent()));
        xmlEffectiveDateFrom = ((TimePeriodType) localEpisodicEventTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom),
                equalTo(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC)));
        xmlEffectiveDateTo = ((TimePeriodType) localEpisodicEventTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getEndPosition().getValue().get(0);
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo),
                equalTo(GMLDateUtils.dateToString(logItem.getEffectiveDates().getTo(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC)));
    }

}

