package au.gov.ga.geodesy.support.mapper.dozer.populator;

import java.time.Instant;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.xml.icsm.geodesyml.v_0_3.CollocationInformationType;

public class CollocationInformationTypePopulator extends GeodesyMLElementPopulator<CollocationInformationType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param moreInformationType
     */
    @Override
    public void checkAllRequiredElementsPopulated(CollocationInformationType moreInformationType) {
        checkElementPopulated(moreInformationType, "instrumentationType", GMLGmlTools.getEmptyCodeType());
        checkElementPopulated(moreInformationType, "status", GMLGmlTools.getEmptyCodeType());
        checkElementPopulated(moreInformationType, "validTime",
                TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(Instant.EPOCH));
    }
}
