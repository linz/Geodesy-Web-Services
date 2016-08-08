package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.OtherInstrumentationLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.OtherInstrumentationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the mapping of a GeodesyML otherInstrumentationLogItem element
 * to and from an OtherInstrumentationLogItem domain object.
 */
public class OtherInstrumentationMapperTest {

    private OtherInstrumentationMapper mapper = new OtherInstrumentationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("QIKI-otherInstrumentation"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        OtherInstrumentationType otherInstrumentationTypeA = siteLog.getOtherInstrumentations().get(0).getOtherInstrumentation();

        OtherInstrumentationLogItem logItem = mapper.to(otherInstrumentationTypeA);
        assertThat(logItem.getInstrumentation(), equalTo(otherInstrumentationTypeA.getInstrumentation()));
        String xmlEffectiveDateFrom = ((TimePeriodType) otherInstrumentationTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertThat(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
            equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom)));

        OtherInstrumentationType otherInstrumentationTypeB = mapper.from(logItem);
        assertThat(logItem.getInstrumentation(), equalTo(otherInstrumentationTypeB.getInstrumentation()));
        xmlEffectiveDateFrom = ((TimePeriodType) otherInstrumentationTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom),
            equalTo(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC)));
    }

}

