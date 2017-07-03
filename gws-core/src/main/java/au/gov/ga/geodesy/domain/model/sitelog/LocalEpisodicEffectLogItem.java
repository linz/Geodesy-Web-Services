package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/localInterferences/2564/localEvents.xsd:localEpisodicEventsType
 */
@Entity
@Table(name = "SITELOG_LOCALEPISODICEFFECT")
public class LocalEpisodicEffectLogItem extends LogItem {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGLOCALEPISODICEFFECT")
    private Integer id;

    @Valid
    @Embedded
    protected EffectiveDates effectiveDates;

    @Size(max = 256)
    @Column(name = "EVENT", length = 256)
    protected String event;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
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
     * Return event.
     */
    public String getEvent() {
        return event;
    }

    /**
     * Set event.
     */
    public void setEvent(String value) {
        this.event = value;
    }

    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
