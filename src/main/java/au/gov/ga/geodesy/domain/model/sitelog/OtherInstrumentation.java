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
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2004/otherInstrumentation.xsd
 */
@Entity
@Table(name = "SITELOG_OTHERINSTRUMENTATION")
public class OtherInstrumentation {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGOTHERINSTRUMENT")
    private Integer id;

    @Size(max = 4000)
    @Column(name = "INSTRUMENTATION", length = 4000)
    protected String instrumentation;

    @Valid
    @Embedded
    protected EffectiveDates effectiveDates;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Return instrumentation.
     */
    public String getInstrumentation() {
        return instrumentation;
    }

    /**
     * Set instrumentation.
     */
    public void setInstrumentation(String value) {
        this.instrumentation = value;
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
}
