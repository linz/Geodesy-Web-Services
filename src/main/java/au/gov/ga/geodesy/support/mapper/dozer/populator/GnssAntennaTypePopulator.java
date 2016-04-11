package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaType;

/**
 * The receivers have required elements that don't all exist in the SOPAC Sitelog xml. This fills them in.
 * Note that it isn't possible to do every element as they will have complex element hierarchies.
 * 
 * @author brookes
 *
 */
public class GnssAntennaTypePopulator extends GeodesyMLElementPopulator<GnssAntennaType> {
    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    void checkAllRequiredElementsPopulated(GnssAntennaType gnssAntennaType) {
        // This can be blank when receiver hasn't been removed. Some other logic in the project
        // removes empty elements from the Sopac SiteLog before it gets to this translator
        checkElementPopulated(gnssAntennaType, "antennaCableType", GMLMiscTools.getEmptyString());
        checkElementPopulated(gnssAntennaType, "radomeSerialNumber", GMLMiscTools.getEmptyString());
        checkElementPopulated(gnssAntennaType, "dateRemoved", GMLGmlTools.getEmptyTimePositionType());
    }
}