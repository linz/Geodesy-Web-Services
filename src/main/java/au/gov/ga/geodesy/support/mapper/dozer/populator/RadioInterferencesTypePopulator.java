package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.RadioInterferencesType;

public class RadioInterferencesTypePopulator extends GeodesyMLElementPopulator<RadioInterferencesType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(RadioInterferencesType radioInterferencesType) {
        checkElementPopulated(radioInterferencesType, "observedDegradations", GMLMiscTools.getEmptyString());
    }
}
