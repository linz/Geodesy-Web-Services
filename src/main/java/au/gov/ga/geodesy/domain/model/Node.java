package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;

@Entity
@Table(name = "NODE")
public class Node {

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

    @Column(name = "SITE_ID", nullable = false)
    private Integer siteId;

    @Column(name = "SETUP_ID")
    private Integer setupId;

    @Embedded
    private EffectiveDates effectivePeriod;

    @SuppressWarnings("unused") // used by hibernate
    private Node() {
    }

    public Node(Integer siteId, EffectiveDates p) {
        setSiteId(siteId);
        setEffectivePeriod(p);
    }

    public Node(Integer siteId, EffectiveDates p, Integer setupId) {
        this(siteId, p);
        setSetupId(setupId);
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getSetupId() {
        return setupId;
    }

    public void setSetupId(Integer setupId) {
        this.setupId = setupId;
    }

    public EffectiveDates getEffectivePeriod() {
        return effectivePeriod;
    }

    private void setEffectivePeriod(EffectiveDates effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }
}
