package au.gov.ga.geodesy.support.mapper.dozer;

import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardType;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class GeodesyMLSiteLogTranslatorTest {

    GeodesyMLSiteLogDozerTranslator geodesyMLSiteLogTranslator = new GeodesyMLSiteLogDozerTranslator();

    @Test
    public void testTranslateBasedOnChildType() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        FrequencyStandardPropertyType frequencyStandardPropertyType = new FrequencyStandardPropertyType();
        FrequencyStandardType frequencyStandardType = new FrequencyStandardType();
        frequencyStandardType.setId("99");

        geodesyMLSiteLogTranslator.setBasedOnChildType(frequencyStandardPropertyType, frequencyStandardType);

        assertThat(frequencyStandardPropertyType.getFrequencyStandard(), notNullValue());
        assertThat(frequencyStandardPropertyType.getFrequencyStandard().getId(), is("99"));
    }

}
