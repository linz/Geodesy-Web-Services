package au.gov.ga.geodesy.domain.model.event;

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

import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(name = "DOMAIN_EVENT")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "EVENT_NAME")
@Configurable
public abstract class Event implements Cloneable {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_EVENT")
    private Integer id;

    @Column(name = "TIME_RAISED", nullable = false)
    private Date timeRaised;

    @Column(name = "SUBSCRIBER", nullable = false)
    public String subscriber;

    @Column(name = "TIME_HANDLED")
    public Date timeHandled;

    @Column(name = "TIME_PUBLISHED")
    public Date timePublished;

    @Column(name = "RETRIES")
    public Integer retries;

    @Transient
    /**
     * Return a Human digestable message about this event. Used in email for example.
     * 
     * @return the message
     */
    public String getMessage() {
        String message = "Event: " + this.getClass().getSimpleName() + ", Time Raised: " + this.getEventTime();
        return message;
    }

    public Event() {
        setTimeRaised(new Date());
    }

    public Date getEventTime() {
        return timeRaised;
    }

    private void setTimeRaised(Date t) {
        timeRaised = t;
    }

    public String getSubscriber() {
        return subscriber;
    }

    protected void setSubscriber(String s) {
        subscriber = s;
    }

    public Date getTimeHandled() {
        return timeHandled;
    }

    private void setTimeHandled(Date t) {
        timeHandled = t;
    }

    public Date getTimePublished() {
        return timePublished;
    }

    public void setTimePublished(Date timePublished) {
        this.timePublished = timePublished;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public void published() {
        if (getTimePublished() != null) {
            setRetries(getRetries() == null ? 1 : getRetries() + 1);
        }
        setTimePublished(new Date());
    }

    public void handled() {
        if (timeHandled == null) {
            setTimeHandled(new Date());
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
