package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/collocationInformation.xsd:collocationInformationType
 */
@Entity
@Table(name = "SITELOG_COLLOCATIONINFORMATION")
public class CollocationInformationLogItem extends LogItem {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGCOLLOCATIONINFO")
    private Integer id;

    @Size(max = 256)
    @Column(name = "INSTRUMENT_TYPE", length = 256)
    protected String instrumentType;

    @Size(max = 256)
    @Column(name = "STATUS", length = 256)
    protected String status;

    @Valid
    @Embedded
    protected EffectiveDates effectiveDates;

    @Size(max = 256)
    @Column(name = "NOTES", length = 256)
    protected String notes;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Return instrumentation type.
     */
    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     * Set instrumentation type.
     */
    public void setInstrumentType(String value) {
        this.instrumentType = value;
    }

    /**
     * Return status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set status.
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Return effective dates.
     */
    public EffectiveDates getEffectiveDates() {
        return effectiveDates;
    }

    /**
     * Set effective dates.
     */
    public void setEffectiveDates(EffectiveDates value) {
        this.effectiveDates = value;
    }

    /**
     * Return notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set notes.
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
