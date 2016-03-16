package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/localInterferences/2564/localEvents.xsd:localEpisodicEventsType
 */
@Entity
@Table(name = "SITELOG_LOCALEPISODICEVENT")
public class LocalEpisodicEvent {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGLOCALEPISODICEVENT")
    private Integer id;

    @Size(max = 256)
    @Column(name = "EVENT_DATE", length = 256)
    protected String date;

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
     * Return date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Set date.
     */
    public void setDate(String value) {
        this.date = value;
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
}
