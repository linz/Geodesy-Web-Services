package au.gov.ga.geodesy.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.rest.core.config.Projection;

import au.gov.ga.geodesy.support.spring.EntityId;

@Entity
@Table(name = "GNSS_RECEIVER_CONFIGURATION")
public class GnssReceiverConfiguration extends EquipmentConfiguration {

    @Column(name = "FIRMWARE_VERSION")
    private String firmwareVersion;

    @Column(name = "SATELLITE_SYSTEM")
    private String satelliteSystem;

    @Column(name = "ELEVATION_CUTOFF_SETTING")
    private String elevetionCutoffSetting;

    @Column(name = "TEMPERATURE_STABILIZATION")
    private String temperatureStabilization;

    @Column(name = "NOTES", length = 4000)
    private String notes;

    /* @SuppressWarnings("unused") // used by hibernate */
    private GnssReceiverConfiguration() {
        super(null, null);
    }

    public GnssReceiverConfiguration(Integer gnssReceiverId, Date configurationTime) {
        super(gnssReceiverId, configurationTime);
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getSatelliteSystem() {
        return satelliteSystem;
    }

    public void setSatelliteSystem(String satelliteSystem) {
        this.satelliteSystem = satelliteSystem;
    }

    public String getElevetionCutoffSetting() {
        return elevetionCutoffSetting;
    }

    public void setElevetionCutoffSetting(String elevetionCutoffSetting) {
        this.elevetionCutoffSetting = elevetionCutoffSetting;
    }

    public String getTemperatureStabilization() {
        return temperatureStabilization;
    }

    public void setTemperatureStabilization(String temperatureStabilization) {
        this.temperatureStabilization = temperatureStabilization;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Projection(name = "test", types = {EquipmentConfiguration.class, GnssReceiver.class})
    public interface GnssReceiverProjection {
        Equipment getEquipment();
        String getFirmwareVersion();
    }
}
