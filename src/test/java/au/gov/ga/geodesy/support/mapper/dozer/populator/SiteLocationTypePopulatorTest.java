package au.gov.ga.geodesy.support.mapper.dozer.populator;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.utils.GMLGeoTools;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType.ApproximatePositionITRF;

/**
 * Use this to test GeodesyMLElementPopulator (parent) and specifically SiteLocationType.approximatePositionITRF
 * which is a class with its own fields. Since need to compare each of these.
 * 
 * @author brookes
 *
 */
public class SiteLocationTypePopulatorTest {
    SiteLocationTypePopulator siteLocationTypePopulator = new SiteLocationTypePopulator();

    @Test
    public void testApproximatePositionITRF() {
        SiteLocationType siteLocationType = new SiteLocationType();

        ApproximatePositionITRF approximatePositionITRF = new ApproximatePositionITRF();
        siteLocationType.setApproximatePositionITRF(approximatePositionITRF);
        Assert.assertNull(approximatePositionITRF.getLatitudeNorth());
        Assert.assertNull(approximatePositionITRF.getLongitudeEast());
        Assert.assertNull(approximatePositionITRF.getElevationMEllips());
        Assert.assertNull(approximatePositionITRF.getXCoordinateInMeters());
        Assert.assertNull(approximatePositionITRF.getYCoordinateInMeters());
        Assert.assertNull(approximatePositionITRF.getZCoordinateInMeters());

        ApproximatePositionITRF defaultUsed = GMLGeoTools.buildZeroApproximatePositionITRF();
        siteLocationTypePopulator.checkAllRequiredElementsPopulated(siteLocationType);

        Assert.assertNotNull(approximatePositionITRF.getLatitudeNorth());
        Assert.assertEquals(defaultUsed.getLatitudeNorth(), approximatePositionITRF.getLatitudeNorth());
        Assert.assertEquals(defaultUsed.getLongitudeEast(), approximatePositionITRF.getLongitudeEast());
        Assert.assertEquals(defaultUsed.getElevationMEllips(), approximatePositionITRF.getElevationMEllips());
        Assert.assertEquals(defaultUsed.getXCoordinateInMeters(), approximatePositionITRF.getXCoordinateInMeters());
        Assert.assertEquals(defaultUsed.getYCoordinateInMeters(), approximatePositionITRF.getYCoordinateInMeters());
        Assert.assertEquals(defaultUsed.getZCoordinateInMeters(), approximatePositionITRF.getZCoordinateInMeters());
    }

}
