package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.BaseSensorEquipmentType;

/**
 * The receivers have required elements that don't all exist in the SOPAC Sitelog xml. This fills them in.
 * Note that it isn't possible to do every element as they will have complex element hierarchies.
 * 
 * Being used for all subtypes of BaseSensorEquipmentType:
 * - HumiditySensorType
 * - PressureSensorType
 * - TemperatureSensorType
 * - WaterVaporSensorType
 * 
 * @author brookes
 *
 */
public class SensorTypesPopulator extends GeodesyMLElementPopulator<BaseSensorEquipmentType> {
    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    void checkAllRequiredElementsPopulated(BaseSensorEquipmentType sensorType) {
        checkElementPopulated(sensorType, "calibrationDate", GMLGmlTools.getEmptyTimePositionType());
        checkElementPopulated(sensorType, "manufacturer", GMLMiscTools.getEmptyString());
        checkElementPopulated(sensorType, "serialNumber", GMLMiscTools.getEmptyString());
        checkElementPopulated(sensorType, "type", GMLGmlTools.getEmptyCodeType());
        checkElementPopulated(sensorType, "heightDiffToAntenna", GMLMiscTools.getEmptyDouble());
    }
}
