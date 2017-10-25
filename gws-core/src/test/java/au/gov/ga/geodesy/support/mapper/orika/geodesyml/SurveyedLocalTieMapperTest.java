package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.SurveyedLocalTieLogItem;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.spring.UnitTest;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_5.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteLogType;
import au.gov.xml.icsm.geodesyml.v_0_5.SurveyedLocalTieType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Tests the mapping of a GeodesyML SurveyedLocalTieType element
 * to and from a SurveyedLocalTie domain object.
 */
public class SurveyedLocalTieMapperTest extends UnitTest {

    @Autowired
    private SurveyedLocalTieMapper mapper;

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("MOBS"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        SurveyedLocalTieType surveyedLocalTiesA = siteLogType.getSurveyedLocalTies().get(0).getSurveyedLocalTie();
        SurveyedLocalTieLogItem surveyedLocalTie = mapper.to(surveyedLocalTiesA);
        assertThat(surveyedLocalTie.getTiedMarkerName(), is(surveyedLocalTiesA.getTiedMarkerName()));
        assertThat(surveyedLocalTie.getTiedMarkerUsage(), is(surveyedLocalTiesA.getTiedMarkerUsage()));
        assertThat(surveyedLocalTie.getTiedMarkerCdpNumber(), is(surveyedLocalTiesA.getTiedMarkerCDPNumber()));
        assertThat(surveyedLocalTie.getTiedMarkerDomesNumber(), is(surveyedLocalTiesA.getTiedMarkerDOMESNumber()));
        assertThat(surveyedLocalTie.getDifferentialFromMarker().getDx().doubleValue(), is(surveyedLocalTiesA.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDx()));
        assertThat(surveyedLocalTie.getDifferentialFromMarker().getDy().doubleValue(), is(surveyedLocalTiesA.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDy()));
        assertThat(surveyedLocalTie.getDifferentialFromMarker().getDz().doubleValue(), is(surveyedLocalTiesA.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDz()));
        assertThat(Double.parseDouble(surveyedLocalTie.getLocalSiteTieAccuracy()), is(surveyedLocalTiesA.getLocalSiteTiesAccuracy()));
        assertThat(surveyedLocalTie.getSurveyMethod(), is(surveyedLocalTiesA.getSurveyMethod()));
        assertThat(surveyedLocalTie.getDateMeasured(), is(GMLDateUtils.stringToDateMultiParsers(surveyedLocalTiesA.getDateMeasured().getValue().get(0))));
        assertThat(surveyedLocalTie.getNotes(), is(surveyedLocalTiesA.getNotes()));

        SurveyedLocalTieType surveyedLocalTiesB = mapper.from(surveyedLocalTie);
        assertThat(surveyedLocalTiesB.getTiedMarkerName(), is(surveyedLocalTie.getTiedMarkerName()));
        assertThat(surveyedLocalTiesB.getTiedMarkerUsage(), is(surveyedLocalTie.getTiedMarkerUsage()));
        assertThat(surveyedLocalTiesB.getTiedMarkerCDPNumber(), is(surveyedLocalTie.getTiedMarkerCdpNumber()));
        assertThat(surveyedLocalTiesB.getTiedMarkerDOMESNumber(), is(surveyedLocalTie.getTiedMarkerDomesNumber()));
        assertThat(surveyedLocalTiesB.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDx(), is(surveyedLocalTie.getDifferentialFromMarker().getDx().doubleValue()));
        assertThat(surveyedLocalTiesB.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDy(), is(surveyedLocalTie.getDifferentialFromMarker().getDy().doubleValue()));
        assertThat(surveyedLocalTiesB.getDifferentialComponentsGNSSMarkerToTiedMonumentITRS().getDz(), is(surveyedLocalTie.getDifferentialFromMarker().getDz().doubleValue()));
        assertThat(surveyedLocalTiesB.getLocalSiteTiesAccuracy(), is(Double.parseDouble(surveyedLocalTie.getLocalSiteTieAccuracy())));
        assertThat(surveyedLocalTiesB.getSurveyMethod(), is(surveyedLocalTie.getSurveyMethod()));
        assertThat(GMLDateUtils.stringToDateMultiParsers(surveyedLocalTiesB.getDateMeasured().getValue().get(0)), is(surveyedLocalTie.getDateMeasured()));
        assertThat(surveyedLocalTiesB.getNotes(), is(surveyedLocalTie.getNotes()));
    }
}
