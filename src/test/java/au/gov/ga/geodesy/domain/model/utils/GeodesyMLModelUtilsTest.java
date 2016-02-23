package au.gov.ga.geodesy.domain.model.utils;

import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBElement;

import org.junit.Assert;
import org.junit.Test;

import au.gov.xml.icsm.geodesyml.v_0_2_2.SiteLogType;

public class GeodesyMLModelUtilsTest {

    @Test
    public void testBuildJAXBElementEquivalent() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SiteLogType siteLogType = new SiteLogType();
        siteLogType.setId("999");
        JAXBElement<SiteLogType> jaxBEquiv = GeodesyMLModelUtils.buildJAXBElementEquivalent(siteLogType);
        
        Assert.assertNotNull(jaxBEquiv);
        Assert.assertEquals("999", jaxBEquiv.getValue().getId());
    }

}
