package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.io.FileReader;
import java.io.Reader;

import org.springframework.util.ResourceUtils;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLocation;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLogType;

public class SiteLocationMapperTest {

    private SiteLocationMapper mapper = new SiteLocationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from SiteLocationType to SiteLocation and back
     * to SiteLocationType.
     **/
    @Test
    public void testMapping() throws Exception {
        try (Reader mobs = new FileReader(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS.xml"))) {
            GeodesyMLType geodesyML = marshaller.unmarshal(mobs, GeodesyMLType.class).getValue();
            SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(geodesyML.getElements(), SiteLogType.class)
                .findFirst()
                .get();
            SiteLocationType siteLocTypeA = siteLogType.getSiteLocation();

            SiteLocation siteLoc = mapper.to(siteLocTypeA);

            assertEquals(siteLoc.getCity(), siteLocTypeA.getCity());
            assertEquals(siteLoc.getState(), siteLocTypeA.getState());
            assertEquals(siteLoc.getCountry(), siteLocTypeA.getCountryCodeISO());
            assertEquals(siteLoc.getTectonicPlate(), siteLocTypeA.getTectonicPlate().getValue());
            assertEquals(siteLoc.getApproximatePosition().getItrfX(), Double.valueOf(siteLocTypeA.getApproximatePositionITRF().getXCoordinateInMeters()));
            assertEquals(siteLoc.getApproximatePosition().getItrfY(), Double.valueOf(siteLocTypeA.getApproximatePositionITRF().getYCoordinateInMeters()));
            assertEquals(siteLoc.getApproximatePosition().getItrfZ(), Double.valueOf(siteLocTypeA.getApproximatePositionITRF().getZCoordinateInMeters()));
            assertEquals(siteLoc.getApproximatePosition().getElevationGrs80(), siteLocTypeA.getApproximatePositionITRF().getElevationMEllips());
            assertEquals(siteLoc.getNotes(), siteLocTypeA.getNotes());

            SiteLocationType siteLocTypeB = mapper.from(siteLoc);

            assertEquals(siteLocTypeB.getCity(), siteLoc.getCity());
            assertEquals(siteLocTypeB.getState(), siteLoc.getState());
            assertEquals(siteLocTypeB.getCountryCodeISO(), siteLoc.getCountry());
            assertEquals(siteLocTypeB.getTectonicPlate().getValue(), siteLoc.getTectonicPlate());
            assertEquals(siteLocTypeB.getTectonicPlate().getCodeSpace(), "eGeodesy/tectonicPlate");
            assertEquals(siteLocTypeB.getApproximatePositionITRF().getXCoordinateInMeters(), String.valueOf(siteLoc.getApproximatePosition().getItrfX()));
            assertEquals(siteLocTypeB.getApproximatePositionITRF().getYCoordinateInMeters(), String.valueOf(siteLoc.getApproximatePosition().getItrfY()));
            assertEquals(siteLocTypeB.getApproximatePositionITRF().getZCoordinateInMeters(), String.valueOf(siteLoc.getApproximatePosition().getItrfZ()));
            assertEquals(siteLocTypeB.getApproximatePositionITRF().getElevationMEllips(), siteLoc.getApproximatePosition().getElevationGrs80());
            assertEquals(siteLocTypeB.getNotes(), siteLoc.getNotes());
        }
    }
}
