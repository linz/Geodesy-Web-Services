package au.gov.ga.geodesy.domain.model.equipment;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EQUIPMENT_CONFIGURATION")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EquipmentConfiguration {

    @Id
    @Column(name = "EQUIPMENT_CONFIGURATION_ID")
    @GeneratedValue(generator = "surrogateKeyGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private @MonotonicNonNull Integer id;

    @Column(name = "EQUIPMENT_ID", nullable = false)
    @JsonIgnore
    private Integer equipmentId;

    @Column(name = "CONFIGURATION_TIME")
    @JsonIgnore
    private @MonotonicNonNull Instant configurationTime;

    @SuppressWarnings({"unused", "initialization.fields.uninitialized"}) // used by hibernate
    private EquipmentConfiguration() {
    }

    public EquipmentConfiguration(Integer equipmentId, Instant configurationTime) {
        this.equipmentId = equipmentId;
        this.configurationTime = configurationTime;
    }

    public @Nullable Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public @Nullable Instant getConfigurationTime() {
        return configurationTime;
    }
}
