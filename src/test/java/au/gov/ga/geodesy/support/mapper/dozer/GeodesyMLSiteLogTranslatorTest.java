package au.gov.ga.geodesy.support.mapper.dozer;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.support.mapper.dozer.GeodesyMLSiteLogDozerTranslator;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardType;

public class GeodesyMLSiteLogTranslatorTest {

    GeodesyMLSiteLogDozerTranslator geodesyMLSiteLogTranslator = new GeodesyMLSiteLogDozerTranslator();
    
    @Test
    public void testTranslateBasedOnChildType() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        FrequencyStandardPropertyType frequencyStandardPropertyType = new FrequencyStandardPropertyType();
        FrequencyStandardType frequencyStandardType = new FrequencyStandardType();
        frequencyStandardType.setId("99");
        
        geodesyMLSiteLogTranslator.setBasedOnChildType(frequencyStandardPropertyType, frequencyStandardType);
        
        Assert.assertNotNull(frequencyStandardPropertyType.getFrequencyStandard());
        Assert.assertEquals("99", frequencyStandardPropertyType.getFrequencyStandard().getId());
    }

}
