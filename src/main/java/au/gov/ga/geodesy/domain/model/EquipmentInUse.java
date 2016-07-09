package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;

@Entity
@Table(name = "EQUIPMENT_IN_USE")
public class EquipmentInUse {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @JsonIgnore
    @Column(name = "EQUIPMENT_ID", nullable = false)
    private Integer equipmentId;

    @JsonIgnore
    @Column(name = "EQUIPMENT_CONFIGURATION_ID", nullable = false)
    private Integer configurationId;

    @Embedded
    private EffectiveDates period;

    @SuppressWarnings("unused") // used by hibernate
    private EquipmentInUse() {
    }

    public EquipmentInUse(Integer equipmentId, Integer configurationId, EffectiveDates period) {
        setEquipmentId(equipmentId);
        setConfigurationId(configurationId);
        setPeriod(period);
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Integer configurationId) {
        this.configurationId = configurationId;
    }

    public EffectiveDates getPeriod() {
        return period;
    }

    public void setPeriod(EffectiveDates period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }
        if (x == this) {
            return true;
        }
        if (x.getClass() != getClass()) {
            return false;
        }
        EquipmentInUse other = (EquipmentInUse) x;
        return new EqualsBuilder()
            .append(equipmentId, other.getEquipmentId())
            .append(configurationId, other.getConfigurationId())
            .append(period, other.getPeriod())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
            append(equipmentId).
            append(configurationId).
            append(period).
            toHashCode();
   }
}
