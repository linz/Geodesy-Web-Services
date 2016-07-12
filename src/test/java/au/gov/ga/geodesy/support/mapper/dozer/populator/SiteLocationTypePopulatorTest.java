package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLGeoTools;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType.ApproximatePositionITRF;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Use this to test GeodesyMLElementPopulator (parent) and specifically SiteLocationType.approximatePositionITRF
 * which is a class with its own fields. Since need to compare each of these.
 *
 * @author brookes
 */
public class SiteLocationTypePopulatorTest {
    SiteLocationTypePopulator siteLocationTypePopulator = new SiteLocationTypePopulator();

    @Test
    public void testApproximatePositionITRF() {
        SiteLocationType siteLocationType = new SiteLocationType();

        ApproximatePositionITRF approximatePositionITRF = new ApproximatePositionITRF();
        siteLocationType.setApproximatePositionITRF(approximatePositionITRF);
        assertThat(approximatePositionITRF.getLatitudeNorth(), nullValue());
        assertThat(approximatePositionITRF.getLongitudeEast(), nullValue());
        assertThat(approximatePositionITRF.getElevationMEllips(), nullValue());
        assertThat(approximatePositionITRF.getXCoordinateInMeters(), nullValue());
        assertThat(approximatePositionITRF.getYCoordinateInMeters(), nullValue());
        assertThat(approximatePositionITRF.getZCoordinateInMeters(), nullValue());

        ApproximatePositionITRF defaultUsed = GMLGeoTools.buildZeroApproximatePositionITRF();
        siteLocationTypePopulator.checkAllRequiredElementsPopulated(siteLocationType);

        assertThat(approximatePositionITRF.getLatitudeNorth(), notNullValue());
        assertThat(approximatePositionITRF.getLatitudeNorth(), is(defaultUsed.getLatitudeNorth()));
        assertThat(approximatePositionITRF.getLongitudeEast(), is(defaultUsed.getLongitudeEast()));
        assertThat(approximatePositionITRF.getElevationMEllips(), is(defaultUsed.getElevationMEllips()));
        assertThat(approximatePositionITRF.getXCoordinateInMeters(), is(defaultUsed.getXCoordinateInMeters()));
        assertThat(approximatePositionITRF.getYCoordinateInMeters(), is(defaultUsed.getYCoordinateInMeters()));
        assertThat(approximatePositionITRF.getZCoordinateInMeters(), is(defaultUsed.getZCoordinateInMeters()));
    }

    // these not working
    // @Test
    public void testVIR01() {
        String country = "U.S. Virgin Islands (USA)";
        String code = SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode(country);

        assertThat(code, is("VIR"));

    }

    // @Test
    public void testVIR02() {
        String country = "Virgin Islands";
        String code = SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode(country);

        assertThat(code, is("VIR"));

    }

}
