package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/receiver.xsd:gnssReceiverType
 */
@Entity
@Table(name = "SITELOG_GNSSRECEIVER")
public class GnssReceiverLogItem extends EquipmentLogItem {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGRECEIVER")
    private Integer id;

    @Size(max = 256)
    @Column(name = "RECEIVER_TYPE", length = 256)
    protected String type;

    @Size(max = 256)
    @Column(name = "SATELLITE_SYSTEM", length = 256)
    protected String satelliteSystem;

    @Size(max = 256)
    @Column(name = "SERIAL_NUMBER", length = 256)
    protected String serialNumber;

    @Size(max = 256)
    @Column(name = "FIRMWARE_VERSION", length = 256)
    protected String firmwareVersion;

    @Size(max = 256)
    @Column(name = "ELEVATION_CUTOFF_SETTING", length = 256)
    protected String elevationCutoffSetting;

    @Past
    @Column(name = "DATE_INSTALLED")
    protected Instant dateInstalled;

    @Past
    @Column(name = "DATE_REMOVED")
    protected Instant dateRemoved;

    @Size(max = 256)
    @Column(name = "TEMPERATURE_STABILIZATION", length = 256)
    protected String temperatureStabilization;

    @Size(max = 4000)
    @Column(name = "NOTES", length = 4000)
    protected String notes;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Return receiver type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set receiver type.
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Return satellite system type.
     */
    public String getSatelliteSystem() {
        return satelliteSystem;
    }

    /**
     * Set satellite system type.
     */
    public void setSatelliteSystem(String value) {
        this.satelliteSystem = value;
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
     * Return firmware version.
     */
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    /**
     * Set firmware version.
     */
    public void setFirmwareVersion(String value) {
        this.firmwareVersion = value;
    }

    /**
     * Return elevation cut-off setting.
     */
    public String getElevationCutoffSetting() {
        return elevationCutoffSetting;
    }

    /**
     * Set elevation cut-off setting.
     */
    public void setElevationCutoffSetting(String value) {
        this.elevationCutoffSetting = value;
    }

    /**
     * Return date installed.
     */
    public Instant getDateInstalled() {
        return dateInstalled;
    }

    /**
     * Set date installed.
     */
    public void setDateInstalled(Instant value) {
        this.dateInstalled = value;
    }

    /**
     * Return date removed.
     *
     */
    public Instant getDateRemoved() {
        return dateRemoved;
    }

    /**
     * Set date removed.
     *
     */
    public void setDateRemoved(Instant value) {
        this.dateRemoved = value;
    }

    /**
     * Return temperature stabilization.
     */
    public String getTemperatureStabilization() {
        return temperatureStabilization;
    }

    /**
     * Set temperature stabilization.
     */
    public void setTemperatureStabilization(String value) {
        this.temperatureStabilization = value;
    }

    /**
     * Return notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set notes.
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    public EffectiveDates getEffectiveDates() {
        return new EffectiveDates(getDateInstalled(), getDateRemoved());
    }

    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
