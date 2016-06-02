package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.testng.Assert.assertEquals;

import au.gov.ga.geodesy.domain.model.sitelog.MoreInformation;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import org.testng.annotations.Test;

/**
 * Tests the mapping of a GeodesyML moreInformation element
 * to and from a MoreInformation domain object.
 */
public class MoreInformationMapperTest {
    private MoreInformationMapper mapper = new MoreInformationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from MoreInformationType to MoreInformation and back
     * to MoreInformationType.
     **/
    @Test
    public void testMapping() throws Exception {
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-moreInfo"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        MoreInformationType moreInfoTypeA = siteLogType.getMoreInformation();

        // Test the to mapping
        MoreInformation moreInfo = mapper.to(moreInfoTypeA);
        assertEquals(moreInfo.getPrimaryDataCenter(), moreInfoTypeA.getDataCenter().get(0));
        assertEquals(moreInfo.getSecondaryDataCenter(), moreInfoTypeA.getDataCenter().get(1));
        assertEquals(moreInfo.getUrlForMoreInformation(), moreInfoTypeA.getUrlForMoreInformation());
        assertEquals(moreInfo.getSiteDiagram(), moreInfoTypeA.getSiteDiagram());
        assertEquals(moreInfo.getSiteMap(), moreInfoTypeA.getSiteMap());
        assertEquals(moreInfo.getSitePictures(), moreInfoTypeA.getSitePictures());
        assertEquals(moreInfo.getHorizonMask(), moreInfoTypeA.getHorizonMask());
        assertEquals(moreInfo.getMonumentDescription(), moreInfoTypeA.getMonumentDescription());
        assertEquals(moreInfo.getNotes(), moreInfoTypeA.getNotes());
        assertEquals(moreInfo.getAntennaGraphicsWithDimensions(), moreInfoTypeA.getAntennaGraphicsWithDimensions());
        assertEquals(moreInfo.getInsertTextGraphicFromAntenna(), moreInfoTypeA.getInsertTextGraphicFromAntenna());
        assertEquals(moreInfo.getDoi(), moreInfoTypeA.getDOI().getValue());

        // Test the from mapping
        MoreInformationType moreInfoTypeB = mapper.from(moreInfo);
        assertEquals(moreInfoTypeB.getDataCenter().get(0), moreInfo.getPrimaryDataCenter());
        assertEquals(moreInfoTypeB.getDataCenter().get(1), moreInfo.getSecondaryDataCenter());
        assertEquals(moreInfoTypeB.getUrlForMoreInformation(), moreInfo.getUrlForMoreInformation());
        assertEquals(moreInfoTypeB.getSiteDiagram(), moreInfo.getSiteDiagram());
        assertEquals(moreInfoTypeB.getSiteMap(), moreInfo.getSiteMap());
        assertEquals(moreInfoTypeB.getSitePictures(), moreInfo.getSitePictures());
        assertEquals(moreInfoTypeB.getHorizonMask(), moreInfo.getHorizonMask());
        assertEquals(moreInfoTypeB.getMonumentDescription(), moreInfo.getMonumentDescription());
        assertEquals(moreInfoTypeB.getNotes(), moreInfo.getNotes());
        assertEquals(moreInfoTypeB.getAntennaGraphicsWithDimensions(), moreInfo.getAntennaGraphicsWithDimensions());
        assertEquals(moreInfoTypeB.getInsertTextGraphicFromAntenna(), moreInfo.getInsertTextGraphicFromAntenna());
        assertEquals(moreInfoTypeB.getDOI().getValue(), moreInfo.getDoi());
    }
}
