package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.FrequencyStandardType;

public class FrequencyStandardTypePopulator extends GeodesyMLElementPopulator<FrequencyStandardType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(FrequencyStandardType frequencyStandardType) {
        checkElementPopulated(frequencyStandardType, "standardType", GMLMiscTools.getEmptyList(String.class));
        checkElementPopulated(frequencyStandardType, "validTime", TimePrimitivePropertyTypeUtils
                .buildTimePrimitivePropertyType(TimePrimitivePropertyTypeUtils.buildStartOfTime()));
    }
}
