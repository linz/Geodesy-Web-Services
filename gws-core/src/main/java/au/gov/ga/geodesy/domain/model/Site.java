package au.gov.ga.geodesy.domain.model;

import java.time.Instant;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import au.gov.ga.geodesy.support.java.util.ImpreciseTime;

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

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    // @Embedded
    // @AttributeOverrides({
    //     @AttributeOverride(name="time",
    //                        column=@Column(name="DATE_INSTALLED")),
    //     @AttributeOverride(name="precision",
    //                        column=@Column(name="DATE_INSTALLED_PRECISION"))
    // })
    // private ImpreciseTime dateInstalled;
    @Column(name="DATE_INSTALLED")
    private Instant dateInstalled;

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

    public Instant getDateInstalled() {
        return dateInstalled;
    }

    public void setDateInstalled(Instant dateInstalled) {
        this.dateInstalled = dateInstalled;
    }
}
