package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.FormInformationType;

public class FormInformationTypePopulator extends GeodesyMLElementPopulator<FormInformationType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(FormInformationType formInformationType) {
        checkElementPopulated(formInformationType, "preparedBy", GMLMiscTools.getEmptyString());
        checkElementPopulated(formInformationType, "reportType", GMLMiscTools.getEmptyString());
        checkElementPopulated(formInformationType, "datePrepared", GMLGmlTools.getEmptyTimePositionType());
    }
}
