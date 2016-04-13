package au.gov.ga.geodesy.support.mapper.dozer.populator;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

import au.gov.ga.geodesy.support.mapper.dozer.converter.TimePrimitivePropertyTypeUtils;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;
import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.BaseSensorEquipmentType;
import au.gov.xml.icsm.geodesyml.v_0_3.HumiditySensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.PressureSensorType;
import au.gov.xml.icsm.geodesyml.v_0_3.TemperatureSensorType;

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
        checkElementPopulated(sensorType, "validTime",
                TimePrimitivePropertyTypeUtils.buildTimePrimitivePropertyType(GMLDateUtils.buildStartOfTime()));
        if (sensorType instanceof TemperatureSensorType) {
            checkElementPopulated(sensorType, "accuracyDegreesCelcius", new BigDecimal(0));
        }
        if (sensorType instanceof TemperatureSensorType || sensorType instanceof HumiditySensorType) {
            checkElementPopulated(sensorType, "aspiration", GMLMiscTools.getEmptyString());
        }
        if (sensorType instanceof TemperatureSensorType || sensorType instanceof HumiditySensorType || sensorType instanceof PressureSensorType) {
            checkElementPopulated(sensorType, "dataSamplingInterval", new BigDecimal(0));
        }
    }
}
