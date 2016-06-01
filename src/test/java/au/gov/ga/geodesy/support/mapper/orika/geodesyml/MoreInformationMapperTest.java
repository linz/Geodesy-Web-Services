package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.MoreInformation;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by u43894 on 30/05/2016.
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
        GeodesyMLType mobs = marshaller.unmarshal(TestResources.geodesyMLSiteLogReader("MOBS-sensors"), GeodesyMLType.class)
                .getValue();

        SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(mobs.getElements(), SiteLogType.class)
                .findFirst().get();

        MoreInformationType moreInfoType = siteLogType.getMoreInformation();

        // Assume it is failed if no "geo:moreInformation" element found
        assertNotNull(moreInfoType, "No \"moreInformation\" found under the \"sitelog\" element in the input xml file.");

        // Test the to mapping
        MoreInformation moreInfo = mapper.to(moreInfoType);
        testMappingValues(moreInfoType, moreInfo);

        // Test the from mapping
        MoreInformationType moreInfoTypeFrom = mapper.from(moreInfo);
        testMappingValues(moreInfoTypeFrom, moreInfo);
    }

    private void testMappingValues(MoreInformationType moreInfoType, MoreInformation moreInfo) {
        assertEquals(moreInfo.getPrimaryDataCenter(), moreInfoType.getDataCenter().get(0));
        assertEquals(moreInfo.getSecondaryDataCenter(), moreInfoType.getDataCenter().get(1));

        assertEquals(moreInfo.getUrlForMoreInformation(), moreInfoType.getUrlForMoreInformation());
        assertEquals(moreInfo.getSiteDiagram(), moreInfoType.getSiteDiagram());
        assertEquals(moreInfo.getSiteMap(), moreInfoType.getSiteMap());
        assertEquals(moreInfo.getSitePictures(), moreInfoType.getSitePictures());
        assertEquals(moreInfo.getHorizonMask(), moreInfoType.getHorizonMask());
        assertEquals(moreInfo.getMonumentDescription(), moreInfoType.getMonumentDescription());
        assertEquals(moreInfo.getNotes(), moreInfoType.getNotes());
        assertEquals(moreInfo.getAntennaGraphicsWithDimensions(), moreInfoType.getAntennaGraphicsWithDimensions());
        assertEquals(moreInfo.getInsertTextGraphicFromAntenna(), moreInfoType.getInsertTextGraphicFromAntenna());
        assertEquals(moreInfo.getDoi(), moreInfoType.getDOI().getValue());
    }
}
