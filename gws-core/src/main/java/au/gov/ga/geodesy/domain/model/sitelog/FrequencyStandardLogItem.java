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
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/frequencyStandard.xsd:frequenceStandardType
 */
@Entity
@Table(name = "SITELOG_FREQUENCYSTANDARD")
public class FrequencyStandardLogItem extends EquipmentLogItem {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGFREQUENCYSTANDARD")
    private Integer id;

    @Size(max = 256)
    @Column(name = "TYPE", length = 256)
    protected String type;

    @Size(max = 256)
    @Column(name = "INPUT_FREQUENCY", length = 256)
    protected String inputFrequency;

    @Valid
    @Embedded
    protected EffectiveDates effectiveDates;

    @Size(max = 4000)
    @Column(name = "NOTES", length = 4000)
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
     * Return standard type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set standard type.
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Return input frequency.
     */
    public String getInputFrequency() {
        return inputFrequency;
    }

    /**
     * Set input frequency.
     */
    public void setInputFrequency(String value) {
        this.inputFrequency = value;
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

    public String getSerialNumber() {
        return null;
    }

    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
