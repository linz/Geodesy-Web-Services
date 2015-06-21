package au.gov.ga.geodesy.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.rest.core.config.Projection;

@Entity
@Table(name = "GNSS_RECEIVER_CONFIGURATION")
public class GnssReceiverConfiguration extends EquipmentConfiguration {

    @Column(name = "FIRMWARE_VERSION")
    private String firmwareVersion;

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

    @Projection(name = "test", types = {EquipmentConfiguration.class, GnssReceiver.class})
    public interface GnssReceiverProjection {
        Equipment getEquipment();
        String getFirmwareVersion();
    }
}
