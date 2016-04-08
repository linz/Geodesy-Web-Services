package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.xml.icsm.geodesyml.v_0_3.SurveyedLocalTiesType;

/**
 * The receivers have required elements that don't all exist in the SOPAC Sitelog xml. This fills them in.
 * Note that it isn't possible to do every element as they will have complex element hierarchies.
 * 
 * @author brookes
 *
 */
public class GeodesyMLDozerEventListener_SurveyedLocalTiesType
        extends GeodesyMLElementPopulator<SurveyedLocalTiesType> {
    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    void checkAllRequiredElementsPopulated(SurveyedLocalTiesType surveyedLocalTiesType) {
        checkElementPopulated(surveyedLocalTiesType, "tiedMarkerUsage", getEmptyString());
        checkElementPopulated(surveyedLocalTiesType, "tiedMarkerCDPNumber", getEmptyString());
        checkElementPopulated(surveyedLocalTiesType, "tiedMarkerDOMESNumber", getEmptyString());
        checkElementPopulated(surveyedLocalTiesType, "surveyMethod", getEmptyString());
    }

    private String getEmptyString() {
        return "";
    }
}
