package au.gov.ga.geodesy.domain.model.sitelog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/baseEquipmentLib.xsd:baseSensorEquipmentType"
 */
@MappedSuperclass
public abstract class SensorEquipmentLogItem implements EquipmentLogItem {

    @Size(max = 256)
    @Column(name = "TYPE", length = 256)
    protected String type;

    @Size(max = 256)
    @Column(name = "MANUFACTURER", length = 256)
    protected String manufacturer;

    @Size(max = 256)
    @Column(name = "SERIAL_NUMBER", length = 256)
    protected String serialNumber;

    @Size(max = 256)
    @Column(name = "HEIGHT_DIFF_TO_ANTENNA", length = 256)
    protected String heightDiffToAntenna;

    @Past
    @Column(name = "CALLIBRATION_DATE", length = 256)
    protected Date calibrationDate;

    @Valid
    @Embedded
    protected EffectiveDates effectiveDates;

    /**
     * Return sensor equipment type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set sensor equipment type.
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Return manufacturer.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Set manufacturer.
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * Return serial number.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Set serial number.
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

    /**
     * Return height difference to antenna.
     */
    public String getHeightDiffToAntenna() {
        return heightDiffToAntenna;
    }

    /**
     * Seta height difference to antenna.
     */
    public void setHeightDiffToAntenna(String value) {
        this.heightDiffToAntenna = value;
    }

    /**
     * Return calibration date.
     */
    public Date getCalibrationDate() {
        return calibrationDate;
    }

    /**
     * Set calibration date.
     */
    public void setCalibrationDate(Date value) {
        this.calibrationDate = value;
    }

    /**
     * Return effective dates.
     */
    public EffectiveDates getEffectiveDates() {
        return effectiveDates;
    }

    /**
     * Set effective dates.
     */
    public void setEffectiveDates(EffectiveDates value) {
        this.effectiveDates = value;
    }
}
