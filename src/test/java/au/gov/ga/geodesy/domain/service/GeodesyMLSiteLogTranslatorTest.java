package au.gov.ga.geodesy.domain.service;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import au.gov.xml.icsm.geodesyml.v_0_2_2.FrequencyStandardPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_2_2.FrequencyStandardType;

public class GeodesyMLSiteLogTranslatorTest {

    GeodesyMLSiteLogTranslator geodesyMLSiteLogTranslator = new GeodesyMLSiteLogTranslator();
    
    @Test
    public void testReflectionThing() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        FrequencyStandardPropertyType frequencyStandardPropertyType = new FrequencyStandardPropertyType();
        FrequencyStandardType frequencyStandardType = new FrequencyStandardType();
        frequencyStandardType.setId("99");
        
        geodesyMLSiteLogTranslator.setBasedOnChildType(frequencyStandardPropertyType, frequencyStandardType);
        
        Assert.assertNotNull(frequencyStandardPropertyType.getFrequencyStandard());
        Assert.assertEquals("99", frequencyStandardPropertyType.getFrequencyStandard().getId());
    }

}
