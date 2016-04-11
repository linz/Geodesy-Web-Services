package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssReceiverType;

/**
 * The receivers have required elements that don't all exist in the SOPAC Sitelog xml. This fills them in.
 * Note that it isn't possible to do every element as they will have complex element hierarchies.
 * 
 * @author brookes
 *
 */
public class GnssReceiverTypePopulator extends GeodesyMLElementPopulator<GnssReceiverType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(GnssReceiverType gnssReceiverType) {
        // This element can be blank when receiver hasn't been removed. Some other logic in the project
        // removes empty elements from the Sopac SiteLog before it gets to this translator
        checkElementPopulated(gnssReceiverType, "dateRemoved", GMLGmlTools.getEmptyTimePositionType());

        // GnssReceiverType has a SerialNumber (defined on it) and a manufacturerModel (defined on BaseGeodeticEquipmentType, which is the
        // Grandparent of) and we are uncertain exactly how to handle this multiplicity of what seems to be an element with the same
        // purpose. For the mean time we will have both elements. This code will make sure of this requirement.
        if (gnssReceiverType.getManufacturerSerialNumber() == null) {
            if (gnssReceiverType.getSerialNumber() == null) {
                checkElementPopulated(gnssReceiverType, "serialNumber", GMLMiscTools.getEmptyString());
            }
            checkElementPopulated(gnssReceiverType, "manufacturerSerialNumber", gnssReceiverType.getSerialNumber());
        }
        checkElementPopulated(gnssReceiverType, "serialNumber", gnssReceiverType.getManufacturerSerialNumber());

    }
}
