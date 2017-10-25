package au.gov.ga.geodesy.domain.model.utils;

import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;
import au.gov.xml.icsm.geodesyml.v_0_5.SiteLogType;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class GeodesyMLModelUtilsTest {

    @Test
    public void testBuildJAXBElementEquivalent() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SiteLogType siteLogType = new SiteLogType();
        siteLogType.setId("999");
        JAXBElement<SiteLogType> jaxBEquiv = GeodesyMLUtils.buildJAXBElementEquivalent(siteLogType);

        assertThat(jaxBEquiv, notNullValue());
        assertThat(jaxBEquiv.getValue().getId(), is("999"));
    }

}
