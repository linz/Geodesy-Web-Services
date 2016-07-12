package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import au.gov.ga.geodesy.domain.model.sitelog.FormInformation;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.xml.icsm.geodesyml.v_0_3.FormInformationType;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import org.testng.annotations.Test;


/**
 * Tests the mapping of a GeodesyML FormInformationType element
 * to and from a FormInformation domain object.
 */
public class FormInformationMapperTest {

    private FormInformationMapper mapper = new FormInformationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Test
    public void testMapping() throws Exception {

        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("ALIC"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLog = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        FormInformationType formInfoTypeA = siteLog.getFormInformation();

        FormInformation formInfo = mapper.to(formInfoTypeA);
        assertThat(formInfo.getPreparedBy(), is(formInfoTypeA.getPreparedBy()));
        assertThat(formInfo.getReportType(), is(formInfoTypeA.getReportType()));
        assertThat(formInfo.getDatePrepared(), is(GMLDateUtils.stringToDateMultiParsers(formInfoTypeA.getDatePrepared().getValue().get(0))));

        FormInformationType formInfoTypeB = mapper.from(formInfo);
        assertThat(formInfoTypeB.getPreparedBy(), is(formInfo.getPreparedBy()));
        assertThat(formInfoTypeB.getReportType(), is(formInfo.getReportType()));
        assertThat(GMLDateUtils.stringToDateMultiParsers(formInfoTypeB.getDatePrepared().getValue().get(0)), is(formInfo.getDatePrepared()));
    }
}
