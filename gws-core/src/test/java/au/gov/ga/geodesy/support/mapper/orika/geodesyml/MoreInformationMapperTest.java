package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.MoreInformation;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.ga.geodesy.support.spring.UnitTest;
import au.gov.xml.icsm.geodesyml.v_0_5.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_5.MoreInformationType;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteLogType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the mapping of a GeodesyML moreInformation element
 * to and from a MoreInformation domain object.
 */
public class MoreInformationMapperTest extends UnitTest {

    @Autowired
    private MoreInformationMapper mapper;

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from MoreInformationType to MoreInformation and back
     * to MoreInformationType.
     **/
    @Test
    public void testMapping() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.customGeodesyMLSiteLogReader("MOBS-moreInfo"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        MoreInformationType moreInfoTypeA = siteLogType.getMoreInformation();

        // Test the to mapping
        MoreInformation moreInfo = mapper.to(moreInfoTypeA);
        assertThat(moreInfo.getPrimaryDataCenter(), is(moreInfoTypeA.getDataCenter().get(0)));
        assertThat(moreInfo.getSecondaryDataCenter(), is(moreInfoTypeA.getDataCenter().get(1)));
        assertThat(moreInfo.getUrlForMoreInformation(), is(moreInfoTypeA.getUrlForMoreInformation()));
        assertThat(moreInfo.getSiteDiagram(), is(moreInfoTypeA.getSiteDiagram()));
        assertThat(moreInfo.getSiteMap(), is(moreInfoTypeA.getSiteMap()));
        assertThat(moreInfo.getSitePictures(), is(moreInfoTypeA.getSitePictures()));
        assertThat(moreInfo.getHorizonMask(), is(moreInfoTypeA.getHorizonMask()));
        assertThat(moreInfo.getMonumentDescription(), is(moreInfoTypeA.getMonumentDescription()));
        assertThat(moreInfo.getNotes(), is(moreInfoTypeA.getNotes()));
        assertThat(moreInfo.getAntennaGraphicsWithDimensions(), is(moreInfoTypeA.getAntennaGraphicsWithDimensions()));
        assertThat(moreInfo.getInsertTextGraphicFromAntenna(), is(moreInfoTypeA.getInsertTextGraphicFromAntenna()));
        assertThat(moreInfo.getDoi(), is(moreInfoTypeA.getDOI().getValue()));

        // Test the from mapping
        MoreInformationType moreInfoTypeB = mapper.from(moreInfo);
        assertThat(moreInfoTypeB.getDataCenter().get(0), is(moreInfo.getPrimaryDataCenter()));
        assertThat(moreInfoTypeB.getDataCenter().get(1), is(moreInfo.getSecondaryDataCenter()));
        assertThat(moreInfoTypeB.getUrlForMoreInformation(), is(moreInfo.getUrlForMoreInformation()));
        assertThat(moreInfoTypeB.getSiteDiagram(), is(moreInfo.getSiteDiagram()));
        assertThat(moreInfoTypeB.getSiteMap(), is(moreInfo.getSiteMap()));
        assertThat(moreInfoTypeB.getSitePictures(), is(moreInfo.getSitePictures()));
        assertThat(moreInfoTypeB.getHorizonMask(), is(moreInfo.getHorizonMask()));
        assertThat(moreInfoTypeB.getMonumentDescription(), is(moreInfo.getMonumentDescription()));
        assertThat(moreInfoTypeB.getNotes(), is(moreInfo.getNotes()));
        assertThat(moreInfoTypeB.getAntennaGraphicsWithDimensions(), is(moreInfo.getAntennaGraphicsWithDimensions()));
        assertThat(moreInfoTypeB.getInsertTextGraphicFromAntenna(), is(moreInfo.getInsertTextGraphicFromAntenna()));
        assertThat(moreInfoTypeB.getDOI().getValue(), is(moreInfo.getDoi()));
    }
}
