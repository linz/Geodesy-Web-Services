package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteIdentificationType;

/**
 * The translate simply copied the Countries (in Sopac Sitelog XML) across to the GeoesyML CountryCode elements.
 * This cleans those up by converting to the actual CountryCodes.
 * 
 * @author brookes
 *
 */
public class SiteIdentificationTypePopulator extends GeodesyMLElementPopulator<SiteIdentificationType> {
    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    void checkAllRequiredElementsPopulated(SiteIdentificationType sensorType) {
        checkElementPopulated(sensorType, "siteName", GMLMiscTools.getEmptyString());
        checkElementPopulated(sensorType, "cdpNumber", GMLMiscTools.getEmptyString());
        checkElementPopulated(sensorType, "iersDOMESNumber", GMLMiscTools.getEmptyString());
    }
}
