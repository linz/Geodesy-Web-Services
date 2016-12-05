package au.gov.ga.geodesy.domain.model.equipment;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "CLOCK_CONFIGURATION")
@PrimaryKeyJoinColumn(name = "ID")
public class ClockConfiguration extends EquipmentConfiguration {

    @Column(name = "INPUT_FREQUENCY")
    private String inputFrequency;

    @SuppressWarnings("unused") // used by hibernate
    private ClockConfiguration() {
        this(null, null);
    }

    public ClockConfiguration(Integer equipmentId, Instant configurationTime) {
        super(equipmentId, configurationTime);
    }

    public String getInputFrequency() {
        return inputFrequency;
    }

    public void setInputFrequency(String inputFrequency) {
        this.inputFrequency = inputFrequency;
    }
}
