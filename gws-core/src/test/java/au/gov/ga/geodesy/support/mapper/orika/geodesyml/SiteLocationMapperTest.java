package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.Reader;

import org.testng.annotations.Test;

import com.vividsolutions.jts.geom.Point;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLocation;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_4.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLogType;
import net.opengis.gml.v_3_2_1.PointType;

public class SiteLocationMapperTest {

    private SiteLocationMapper mapper = new SiteLocationMapper();
    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    /**
     * Test mapping from SiteLocationType to SiteLocation and back
     * to SiteLocationType. XML file contains an approximate location in both cartesian and geodetic
     * reference systems.
     **/
    @Test
    public void testMappingCartesianAndGeodeticPosition() throws Exception {
        try (Reader mobs = TestResources.customGeodesyMLSiteLogReader("MOBS-itrf-points")) {
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
            
            Point cartesianPosition = siteLoc.getApproximatePosition().getCartesianPosition();
            PointType cartesianPointType = siteLocTypeA.getApproximatePositionITRF().getCartesianPosition().getPoint();
            assertThat(cartesianPosition.getCoordinate().getOrdinate(0), 
            		equalTo(cartesianPointType.getPos().getValue().get(0)));
            assertThat(cartesianPosition.getCoordinate().getOrdinate(1), 
            		equalTo(cartesianPointType.getPos().getValue().get(1)));
            assertThat(cartesianPosition.getCoordinate().getOrdinate(2), 
            		equalTo(cartesianPointType.getPos().getValue().get(2)));  
            assertThat(cartesianPosition.getSRID(), equalTo(SiteLocationMapper.CARTESIAN_COORDINATES));

            Point geodeticPosition = siteLoc.getApproximatePosition().getGeodeticPosition();
            PointType geodeticPointType = siteLocTypeA.getApproximatePositionITRF().getGeodeticPosition().getPoint();
            assertThat(geodeticPosition.getCoordinate().getOrdinate(0), 
            		equalTo(geodeticPointType.getPos().getValue().get(0)));
            assertThat(geodeticPosition.getCoordinate().getOrdinate(1), 
            		equalTo(geodeticPointType.getPos().getValue().get(1)));
            assertThat(geodeticPosition.getCoordinate().getOrdinate(2), 
            		equalTo(geodeticPointType.getPos().getValue().get(2)));  
            assertThat(geodeticPosition.getSRID(), equalTo(SiteLocationMapper.GEODETIC_COORDINATES));
      
            assertThat(siteLoc.getNotes(), equalTo(siteLocTypeA.getNotes()));

            SiteLocationType siteLocTypeB = mapper.from(siteLoc);

            assertThat(siteLocTypeB.getCity(), equalTo(siteLoc.getCity()));
            assertThat(siteLocTypeB.getState(), equalTo(siteLoc.getState()));
            assertThat(siteLocTypeB.getCountryCodeISO().getValue(), equalTo(siteLoc.getCountry()));
            assertThat(siteLocTypeB.getTectonicPlate().getValue(), equalTo(siteLoc.getTectonicPlate()));
            assertThat(siteLocTypeB.getTectonicPlate().getCodeSpace(), equalTo("eGeodesy/tectonicPlate"));
            
            PointType cartesianPointTypeB = siteLocTypeB.getApproximatePositionITRF().getCartesianPosition().getPoint();
            assertThat(cartesianPointTypeB.getPos().getValue().get(0), 
            		equalTo(cartesianPosition.getCoordinate().getOrdinate(0)));
            assertThat(cartesianPointTypeB.getPos().getValue().get(1), 
            		equalTo(cartesianPosition.getCoordinate().getOrdinate(1)));
            assertThat(cartesianPointTypeB.getPos().getValue().get(2), 
            		equalTo(cartesianPosition.getCoordinate().getOrdinate(2)));

            assertThat(cartesianPointTypeB.getSrsName(), equalTo("EPSG:" + SiteLocationMapper.CARTESIAN_COORDINATES));
            
            PointType geodeticPointTypeB = siteLocTypeB.getApproximatePositionITRF().getGeodeticPosition().getPoint();
            assertThat(geodeticPointTypeB.getPos().getValue().get(0), 
            		equalTo(geodeticPosition.getCoordinate().getOrdinate(0)));
            assertThat(geodeticPointTypeB.getPos().getValue().get(1), 
            		equalTo(geodeticPosition.getCoordinate().getOrdinate(1)));
            assertThat(geodeticPointTypeB.getPos().getValue().get(2), 
            		equalTo(geodeticPosition.getCoordinate().getOrdinate(2)));

            assertThat(geodeticPointTypeB.getSrsName(), equalTo("EPSG:" + SiteLocationMapper.GEODETIC_COORDINATES));
            
            assertThat(siteLocTypeB.getNotes(), equalTo(siteLoc.getNotes()));
        }
    }
    
    /**
     * Test mapping from SiteLocationType to SiteLocation and back. This time with only cartesian position.
     **/
    @Test
    public void testMappingCartesianPositionOnly() throws Exception {
        try (Reader mobs = TestResources.customGeodesyMLSiteLogReader("MOBS-itrf-points-cartesian-only")) {
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
            
            Point cartesianPosition = siteLoc.getApproximatePosition().getCartesianPosition();
            PointType cartesianPointType = siteLocTypeA.getApproximatePositionITRF().getCartesianPosition().getPoint();
            assertThat(cartesianPosition.getCoordinate().getOrdinate(0), 
            		equalTo(cartesianPointType.getPos().getValue().get(0)));
            assertThat(cartesianPosition.getCoordinate().getOrdinate(1), 
            		equalTo(cartesianPointType.getPos().getValue().get(1)));
            assertThat(cartesianPosition.getCoordinate().getOrdinate(2), 
            		equalTo(cartesianPointType.getPos().getValue().get(2)));  
            
            assertThat(siteLoc.getNotes(), equalTo(siteLocTypeA.getNotes()));

            SiteLocationType siteLocTypeB = mapper.from(siteLoc);

            assertThat(siteLocTypeB.getCity(), equalTo(siteLoc.getCity()));
            assertThat(siteLocTypeB.getState(), equalTo(siteLoc.getState()));
            assertThat(siteLocTypeB.getCountryCodeISO().getValue(), equalTo(siteLoc.getCountry()));
            assertThat(siteLocTypeB.getTectonicPlate().getValue(), equalTo(siteLoc.getTectonicPlate()));
            assertThat(siteLocTypeB.getTectonicPlate().getCodeSpace(), equalTo("eGeodesy/tectonicPlate"));
            
            PointType cartesianPointTypeB = siteLocTypeB.getApproximatePositionITRF().getCartesianPosition().getPoint();
            assertThat(cartesianPointTypeB.getPos().getValue().get(0), 
            		equalTo(cartesianPosition.getCoordinate().getOrdinate(0)));
            assertThat(cartesianPointTypeB.getPos().getValue().get(1), 
            		equalTo(cartesianPosition.getCoordinate().getOrdinate(1)));
            assertThat(cartesianPointTypeB.getPos().getValue().get(2), 
            		equalTo(cartesianPosition.getCoordinate().getOrdinate(2)));
            
            assertThat(siteLocTypeB.getNotes(), equalTo(siteLoc.getNotes()));
        }
    }
    
    /**
     * Test mapping from SiteLocationType to SiteLocation and back. This time with only geodetic position.
     **/
    @Test
    public void testMappingGeodeticPositionOnly() throws Exception {
        try (Reader mobs = TestResources.customGeodesyMLSiteLogReader("MOBS-itrf-points-geodetic-only")) {
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
            
            Point geodeticPosition = siteLoc.getApproximatePosition().getGeodeticPosition();
            PointType geodeticPointType = siteLocTypeA.getApproximatePositionITRF().getGeodeticPosition().getPoint();
            assertThat(geodeticPosition.getCoordinate().getOrdinate(0), 
            		equalTo(geodeticPointType.getPos().getValue().get(0)));
            assertThat(geodeticPosition.getCoordinate().getOrdinate(1), 
            		equalTo(geodeticPointType.getPos().getValue().get(1)));
            assertThat(geodeticPosition.getCoordinate().getOrdinate(2), 
            		equalTo(geodeticPointType.getPos().getValue().get(2)));  
            
            assertThat(siteLoc.getNotes(), equalTo(siteLocTypeA.getNotes()));

            SiteLocationType siteLocTypeB = mapper.from(siteLoc);

            assertThat(siteLocTypeB.getCity(), equalTo(siteLoc.getCity()));
            assertThat(siteLocTypeB.getState(), equalTo(siteLoc.getState()));
            assertThat(siteLocTypeB.getCountryCodeISO().getValue(), equalTo(siteLoc.getCountry()));
            assertThat(siteLocTypeB.getTectonicPlate().getValue(), equalTo(siteLoc.getTectonicPlate()));
            assertThat(siteLocTypeB.getTectonicPlate().getCodeSpace(), equalTo("eGeodesy/tectonicPlate"));
            
            PointType geodeticPointTypeB = siteLocTypeB.getApproximatePositionITRF().getGeodeticPosition().getPoint();
            assertThat(geodeticPointTypeB.getPos().getValue().get(0), 
            		equalTo(geodeticPosition.getCoordinate().getOrdinate(0)));
            assertThat(geodeticPointTypeB.getPos().getValue().get(1), 
            		equalTo(geodeticPosition.getCoordinate().getOrdinate(1)));
            assertThat(geodeticPointTypeB.getPos().getValue().get(2), 
            		equalTo(geodeticPosition.getCoordinate().getOrdinate(2)));
            
            assertThat(siteLocTypeB.getNotes(), equalTo(siteLoc.getNotes()));
        }
    }
}
