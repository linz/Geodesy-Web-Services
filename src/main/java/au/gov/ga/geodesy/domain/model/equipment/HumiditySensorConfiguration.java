package au.gov.ga.geodesy.domain.model.equipment;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "HUMIDITY_SENSOR_CONFIGURATION")
@PrimaryKeyJoinColumn(name = "ID")
public class HumiditySensorConfiguration extends EquipmentConfiguration {

    @Column(name = "HEIGHT_DIFF_TO_ANTENNA")
    private String heightDiffToAntenna;

    @Column(name = "NOTES")
    private String notes;

    private HumiditySensorConfiguration() {
        super(null, null);
    }

    public HumiditySensorConfiguration(Integer humiditySensorId, Instant configurationTime) {
        super(humiditySensorId, configurationTime);
    }

    public String getHeightDiffToAntenna() {
        return heightDiffToAntenna;
    }

    public void setHeightDiffToAntenna(String heightDiffToAntenna) {
        this.heightDiffToAntenna = heightDiffToAntenna;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
