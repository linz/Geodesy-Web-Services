package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.GnssAntennaType;
import au.gov.xml.icsm.geodesyml.v_0_3.IgsAntennaModelCodeType;
import au.gov.xml.icsm.geodesyml.v_0_3.IgsRadomeModelCodeType;

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
     * @param gnssAntennaType
     */
    void checkAllRequiredElementsPopulated(GnssAntennaType gnssAntennaType) {
        // This can be blank when receiver hasn't been removed. Some other logic in the project
        // removes empty elements from the Sopac SiteLog before it gets to this translator
        checkElementPopulated(gnssAntennaType, "antennaCableType", GMLMiscTools.getEmptyString());
        checkElementPopulated(gnssAntennaType, "radomeSerialNumber", GMLMiscTools.getEmptyString());
        checkElementPopulated(gnssAntennaType, "dateRemoved", GMLGmlTools.getEmptyTimePositionType());
        checkElementPopulated(gnssAntennaType, "antennaType", getEmptyIgsAntennaModelCodeType());
        checkElementPopulated(gnssAntennaType, "antennaReferencePoint", GMLGmlTools.getEmptyCodeType());
        checkElementPopulated(gnssAntennaType, "antennaRadomeType", getEmptyIgsRadomeModelCodeType());
        checkElementPopulated(gnssAntennaType, "dateInstalled", GMLGmlTools.getEmptyTimePositionType());

        // gnssAntennaType has a SerialNumber (defined on it) and a manufacturerModel (defined on BaseGeodeticEquipmentType, which is the
        // Grandparent of) and we are uncertain exactly how to handle this multiplicity of what seems to be an element with the same
        // purpose. For the mean time we will have both elements. This code will make sure of this requirement.
        if (gnssAntennaType.getManufacturerSerialNumber() == null) {
            if (gnssAntennaType.getSerialNumber() == null) {
                checkElementPopulated(gnssAntennaType, "serialNumber", GMLMiscTools.getEmptyString());
            }
            checkElementPopulated(gnssAntennaType, "manufacturerSerialNumber", gnssAntennaType.getSerialNumber());
        }
        checkElementPopulated(gnssAntennaType, "serialNumber", gnssAntennaType.getManufacturerSerialNumber());
    }

    private IgsRadomeModelCodeType getEmptyIgsRadomeModelCodeType() {
        IgsRadomeModelCodeType igsRadomeModelCodeType = new IgsRadomeModelCodeType();

        // TODO set proper CodeType
        igsRadomeModelCodeType.setCodeSpace("CodeSpace");
        igsRadomeModelCodeType.setValue("CodeValue");
        return igsRadomeModelCodeType;
    }

    private IgsAntennaModelCodeType getEmptyIgsAntennaModelCodeType() {
        IgsAntennaModelCodeType igsAntennaModelCodeType = new IgsAntennaModelCodeType();

        // TODO set proper codeListValueType
        igsAntennaModelCodeType.setCodeList("codelist");
        igsAntennaModelCodeType.setCodeListValue("codeListValue");
        igsAntennaModelCodeType.setCodeSpace("codespace");

        return igsAntennaModelCodeType;
    }
}
