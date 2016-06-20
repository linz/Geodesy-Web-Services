package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.MultipathSourceLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.BasePossibleProblemSourcesType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import net.opengis.gml.v_3_2_1.TimePeriodType;

/**
 * Tests the mapping of a GeodesyML MultipathSourcessPropertyType element
 * to and from an MultipathSourceLogItem domain object.
 */
public class MultipathSourcesMapperTest {

    private MultipathSourcesMapper mapper = new MultipathSourcesMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLTestDataSiteLogReader("METZ-multipathSources"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        BasePossibleProblemSourcesType multipathSourceTypeA =
                siteLog.getMultipathSourcesSet().get(0).getMultipathSources();

        MultipathSourceLogItem logItem = mapper.to(multipathSourceTypeA);
        assertThat(logItem.getPossibleProblemSource(), equalTo(multipathSourceTypeA.getPossibleProblemSources()));

        String xmlEffectiveDateFrom = ((TimePeriodType) multipathSourceTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertThat(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
                equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom)));
        String xmlEffectiveDateTo = ((TimePeriodType) multipathSourceTypeA.getValidTime().getAbstractTimePrimitive().getValue())
                .getEndPosition().getValue().get(0);
        assertThat(GMLDateUtils.dateToString(logItem.getEffectiveDates().getTo(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC),
                equalTo(GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo)));
        assertThat(logItem.getNotes(), equalTo(multipathSourceTypeA.getNotes()));

        BasePossibleProblemSourcesType multipathSourceTypeB = mapper.from(logItem);
        assertThat(multipathSourceTypeB.getPossibleProblemSources(), equalTo(logItem.getPossibleProblemSource()));
        xmlEffectiveDateFrom = ((TimePeriodType) multipathSourceTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getBeginPosition().getValue().get(0);
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateFrom),
                equalTo(GMLDateUtils.dateToString(logItem.getEffectiveDates().getFrom(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC)));
        xmlEffectiveDateTo = ((TimePeriodType) multipathSourceTypeB.getValidTime().getAbstractTimePrimitive().getValue())
                .getEndPosition().getValue().get(0);
        assertThat(
                GMLDateUtils.stringToDateToStringMultiParsers(xmlEffectiveDateTo),
                equalTo(GMLDateUtils.dateToString(logItem.getEffectiveDates().getTo(), GMLDateUtils.GEODESYML_DATE_FORMAT_TIME_MILLISEC)));
        assertThat(multipathSourceTypeB.getPossibleProblemSources(), equalTo(logItem.getPossibleProblemSource()));

    }

}

