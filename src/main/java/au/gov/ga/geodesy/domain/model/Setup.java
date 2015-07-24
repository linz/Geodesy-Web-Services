package au.gov.ga.geodesy.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;

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

    @Column(name = "SITE_ID")
    private Integer siteId; 

    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Embedded
    public EffectiveDates effectivePeriod;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
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
        Setup other = (Setup) x;
        System.out.println(equipmentInUse.getClass());
        System.out.println(other.getEquipmentInUse().getClass());
        return new EqualsBuilder()
            .append(siteId, other.getSiteId())
            .append(name, other.getName())
            .append(getEffectivePeriod(), other.getEffectivePeriod())
            .isEquals()
            &&
            equals(equipmentInUse, other.getEquipmentInUse());
    }

    /**
     * Hibernate uses AbstractPersistentCollections whose equals methods do the
     * wrong thing when called with ordinary Java collections. We disregard the
     * types of colletions and define equality element-wise.
     */
    private boolean equals(Collection<?> as, Collection<?> bs) {
        if (as == bs) {
            return true;
        }
        if (as == null || bs == null) {
            return false;
        }
        if (as.size() != bs.size()) {
            return false;
        }
        Iterator<?> a = as.iterator();
        Iterator<?> b = bs.iterator();
        while (a.hasNext()) {
            if (!new EqualsBuilder().append(a.next(), b.next()).isEquals()) {
                return false;
            }
        }
        return true;
    }
}
