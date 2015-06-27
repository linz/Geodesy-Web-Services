package au.gov.ga.geodesy.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;

@Entity
@Table(name = "SETUP")
public class Setup {

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    protected Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Embedded
    public EffectiveDates effectivePeriod;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SETUP_ID")
    private List<EquipmentInUse> equipmentInUse = new ArrayList<EquipmentInUse>();

    @SuppressWarnings("unused") // used by hibernate
    private Setup() {
    }

    public Setup(String name, EffectiveDates period) {
        setName(name);
        setEffectivePeriod(period);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EffectiveDates getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(EffectiveDates effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public List<EquipmentInUse> getEquipmentInUse() {
        return equipmentInUse;
    }

    public void setEquipmentInUse(List<EquipmentInUse> equipment) {
        this.equipmentInUse = equipment;
    }

    public boolean isCurrent() {
        return getEffectivePeriod().getTo() == null;
    }
}
