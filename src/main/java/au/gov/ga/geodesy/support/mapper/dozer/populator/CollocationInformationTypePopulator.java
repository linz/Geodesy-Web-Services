package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.xml.icsm.geodesyml.v_0_3.CollocationInformationType;

public class CollocationInformationTypePopulator extends GeodesyMLElementPopulator<CollocationInformationType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(CollocationInformationType moreInformationType) {
        checkElementPopulated(moreInformationType, "instrumentationType", GMLGmlTools.getEmptyCodeType());
        checkElementPopulated(moreInformationType, "status", GMLGmlTools.getEmptyCodeType());
        checkElementPopulated(moreInformationType, "validTime",
                TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(GMLDateUtils.buildStartOfTime()));
    }
}
