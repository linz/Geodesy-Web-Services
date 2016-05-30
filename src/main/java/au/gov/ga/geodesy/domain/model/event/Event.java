package au.gov.ga.geodesy.domain.model.event;

import java.time.Instant;

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

    @Transient
    public static final short MAX_RETRIES = 3;

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_EVENT")
    private Integer id;

    @Column(name = "TIME_RAISED", nullable = false)
    private Instant timeRaised;

    @Column(name = "SUBSCRIBER", nullable = false)
    public String subscriber;

    @Column(name = "TIME_HANDLED")
    public Instant timeHandled;

    @Column(name = "TIME_PUBLISHED")
    public Instant timePublished;

    @Column(name = "RETRIES")
    public Integer retries;

    @Column(name = "ERROR")
    private String error;

    /**
     * Return a Human digestable message about this event. Used in email for example.
     *
     * @return the message
     */
    // TODO: could this be toString instead?
    @Transient
    public String getMessage() {
        String message = "Event: " + this.getClass().getSimpleName() + ", Time Raised: " + this.getEventTime();
        return message;
    }

    public Event() {
        setTimeRaised(Instant.now());
    }

    public Instant getEventTime() {
        return timeRaised;
    }

    private void setTimeRaised(Instant t) {
        timeRaised = t;
    }

    public String getSubscriber() {
        return subscriber;
    }

    protected void setSubscriber(String s) {
        subscriber = s;
    }

    public Instant getTimeHandled() {
        return timeHandled;
    }

    private void setTimeHandled(Instant t) {
        timeHandled = t;
    }

    public Instant getTimePublished() {
        return timePublished;
    }

    public void setTimePublished(Instant timePublished) {
        this.timePublished = timePublished;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void published() {
        if (getTimePublished() != null) {
            setRetries(getRetries() == null ? 1 : getRetries() + 1);
        }
        setTimePublished(Instant.now());
    }

    public void handled() {
        if (timeHandled == null) {
            setTimeHandled(Instant.now());
        }
    }

    @Transient
    public boolean hasFailed() {
        // TODO: this test is duplicated in EventRepository
        return getRetries() == MAX_RETRIES;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
