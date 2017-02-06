package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLocation;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_4.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLogType;
import org.testng.annotations.Test;

import java.io.Reader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

public class SiteLocationMapperTest {

    private SiteLocationMapper mapper = new SiteLocationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from SiteLocationType to SiteLocation and back
     * to SiteLocationType.
     **/
    @Test
    public void testMapping() throws Exception {
        try (Reader mobs = TestResources.customGeodesyMLSiteLogReader("MOBS")) {
            GeodesyMLType geodesyML = marshaller.unmarshal(mobs, GeodesyMLType.class).getValue();
            SiteLogType siteLogType = GeodesyMLUtils.getElementFromJAXBElements(geodesyML.getElements(), SiteLogType.class)
                .findFirst()
                .get();
            SiteLocationType siteLocTypeA = siteLogType.getSiteLocation();

            SiteLocation siteLoc = mapper.to(siteLocTypeA);

            assertThat(siteLoc.getCity(), equalTo(siteLocTypeA.getCity()));
            assertThat(siteLoc.getState(), equalTo(siteLocTypeA.getState()));
            assertThat(siteLoc.getCountry(), equalTo(siteLocTypeA.getCountryCodeISO().getValue()));
            assertThat(siteLoc.getTectonicPlate(), equalTo(siteLocTypeA.getTectonicPlate().getValue()));
            assertThat(siteLoc.getApproximatePosition().getItrfX(), closeTo(Double.valueOf(siteLocTypeA.getApproximatePositionITRF()
                .getXCoordinateInMeters()), 0.1));
            assertThat(siteLoc.getApproximatePosition().getItrfY(), closeTo(Double.valueOf(siteLocTypeA.getApproximatePositionITRF()
                .getYCoordinateInMeters()), 0.1));
            assertThat(siteLoc.getApproximatePosition().getItrfZ(), closeTo(Double.valueOf(siteLocTypeA.getApproximatePositionITRF()
                .getZCoordinateInMeters()),0.1));
            assertThat(siteLoc.getApproximatePosition().getElevationGrs80(), equalTo(siteLocTypeA.getApproximatePositionITRF()
                .getElevationMEllips()));
            assertThat(siteLoc.getNotes(), equalTo(siteLocTypeA.getNotes()));

            SiteLocationType siteLocTypeB = mapper.from(siteLoc);

            assertThat(siteLocTypeB.getCity(), equalTo(siteLoc.getCity()));
            assertThat(siteLocTypeB.getState(), equalTo(siteLoc.getState()));
            assertThat(siteLocTypeB.getCountryCodeISO().getValue(), equalTo(siteLoc.getCountry()));
            assertThat(siteLocTypeB.getTectonicPlate().getValue(), equalTo(siteLoc.getTectonicPlate()));
            assertThat(siteLocTypeB.getTectonicPlate().getCodeSpace(), equalTo("eGeodesy/tectonicPlate"));
            assertThat(siteLocTypeB.getApproximatePositionITRF().getXCoordinateInMeters(), equalTo(String.valueOf(siteLoc.getApproximatePosition().getItrfX())));
            assertThat(siteLocTypeB.getApproximatePositionITRF().getYCoordinateInMeters(), equalTo(String.valueOf(siteLoc.getApproximatePosition().getItrfY())));
            assertThat(siteLocTypeB.getApproximatePositionITRF().getZCoordinateInMeters(), equalTo(String.valueOf(siteLoc.getApproximatePosition().getItrfZ())));
            assertThat(siteLocTypeB.getApproximatePositionITRF().getElevationMEllips(), equalTo(siteLoc.getApproximatePosition().getElevationGrs80()));
            assertThat(siteLocTypeB.getNotes(), equalTo(siteLoc.getNotes()));
        }
    }
}
