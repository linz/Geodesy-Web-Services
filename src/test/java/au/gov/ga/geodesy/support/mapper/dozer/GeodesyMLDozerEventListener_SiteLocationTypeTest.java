package au.gov.ga.geodesy.support.mapper.dozer;

import org.junit.Assert;
import org.junit.Test;

public class GeodesyMLDozerEventListener_SiteLocationTypeTest {

    @Test
    public void test01() {
        Assert.assertEquals("AUS", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCode("australia"));
        Assert.assertEquals("AUS", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCode("Australia"));
        Assert.assertEquals("AUS", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCode("AUSTRALIA"));
    }
    
    @Test
    public void test02() {
        Assert.assertEquals("AUSTRALIA", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCountry("aus"));
        Assert.assertEquals("AUSTRALIA", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCountry("Aus"));
        Assert.assertEquals("AUSTRALIA", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCountry("AUS"));
    }
    @Test
    public void test03() {
        Assert.assertEquals("", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCode(""));
        Assert.assertEquals("", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCode(null));
    }
    @Test
    public void test04() {
        Assert.assertEquals("", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCountry(""));
        Assert.assertEquals("", GeodesyMLDozerEventListener_SiteLocationType.COUNTRY_CODES_ALPHA_3.lookupCountry(null));
    }
}
