package au.gov.ga.geodesy.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.igssitelog.domain.model.EventRepository;

@Entity
@Table(name = "DOMAIN_EVENT")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "EVENT_NAME")
@Configurable
public abstract class Event {

    @Autowired(required = true)
    @Transient
    private EventRepository events;

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_EVENT")
    private Integer id;

    @Column(name = "TIME_RAISED", nullable = false)
    private Date timeRaised;

    @Column(name = "TIME_HANDLED")
    public Date timeHandled;

    public Event() {
        setTimeRaised(new Date());
    }

    public Date getEventTime() {
        return timeRaised;
    }

    private void setTimeRaised(Date t) {
        timeRaised = t;
    }

    public Date getTimeHandled() {
        return timeHandled;
    }

    private void setTimeHandled(Date t) {
        timeHandled = t;
    }

    public void handled() {
        if (timeHandled == null) {
            setTimeHandled(new Date());
        }
        if (id != null && events.exists(id)) {
            events.save(this);
        }
    }
}
