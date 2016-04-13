package au.gov.ga.geodesy.support.mapper.dozer;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.mapper.dozer.populator.SiteLocationTypePopulator;

public class GeodesyMLDozerEventListener_SiteLocationTypeTest {

    @Test
    public void test01() {
        Assert.assertEquals("AUS", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode("australia"));
        Assert.assertEquals("AUS", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode("Australia"));
        Assert.assertEquals("AUS", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode("AUSTRALIA"));
    }
    
    @Test
    public void test02() {
        Assert.assertEquals("AUSTRALIA", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry("aus"));
        Assert.assertEquals("AUSTRALIA", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry("Aus"));
        Assert.assertEquals("AUSTRALIA", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry("AUS"));
    }
    @Test
    public void test03() {
        Assert.assertEquals("???", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode(""));
        Assert.assertEquals("???", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCode(null));
    }
    @Test
    public void test04() {
        Assert.assertEquals("???", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry(""));
        Assert.assertEquals("???", SiteLocationTypePopulator.COUNTRY_CODES_ALPHA_3.lookupCountry(null));
    }
}
