package au.gov.ga.geodesy.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EQUIPMENT_CONFIGURATION")
@Inheritance(strategy = InheritanceType.JOINED)
public class EquipmentConfiguration {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @Column(name = "EQUIPMENT_ID", nullable = false)
    @JsonIgnore
    private Integer equipmentId;

    @Column(name = "CONFIGURATION_TIME", nullable = false)
    @JsonIgnore
    private Date configurationTime;

    @SuppressWarnings("unused") // used by hibernate
    private EquipmentConfiguration() {
    }

    public EquipmentConfiguration(Integer equipmentId, Date configurationTime) {
        this.equipmentId = equipmentId;
        this.configurationTime = configurationTime;
    }

    public Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Date getConfigurationTime() {
        return configurationTime;
    }

    public void setConfigurationTime(Date configurationTime) {
        this.configurationTime = configurationTime;
    }
}
