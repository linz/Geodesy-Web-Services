package au.gov.ga.geodesy.support.mapper.dozer;

import au.gov.ga.geodesy.support.mapper.dozer.populator.SiteLocationTypePopulator;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GeodesyMLDozerEventListener_SiteLocationTypeTest {

    @Test
    public void test01() {
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode("australia"), is("AUS"));
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode("Australia"), is("AUS"));
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode("AUSTRALIA"), is("AUS"));
    }

    @Test
    public void test02() {
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry("aus"), is("AUSTRALIA"));
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry("Aus"), is("AUSTRALIA"));
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry("AUS"), is("AUSTRALIA"));
    }

    @Test
    public void test03() {
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode(""), is("???"));
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode(null), is("???"));
    }

    @Test
    public void test04() {
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry(""), is("???"));
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry(null), is("???"));
        assertThat(SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry(null), is("???"));
    }
}
