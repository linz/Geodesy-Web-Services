package au.gov.ga.geodesy.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "SITE")
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class Site {

    @Version
    private Integer version;

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    protected Integer id;

    @Column(name = "SITE_NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("effectivePeriod.from ASC")
    @JoinColumn(name = "SITE_ID")
    private List<Setup> setups = new ArrayList<Setup>();

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Setup> getSetups() {
        return setups;
    }

    /* private void setSetups(List<Setup> setups) { */
    /*     Assert.notNull(setups); */
    /*     this.setups = setups; */
    /* } */
}
