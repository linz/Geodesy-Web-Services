package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "WEEKLY_SOLUTION_AVAILABLE")
@DiscriminatorValue("weekly solution available")
public class WeeklySolutionAvailable extends Event {

    @Column(name = "WEEKLY_SOLUTION_ID", nullable = false)
    private Integer weeklySolutionId;

    @SuppressWarnings("unused") // used by hibernate
    private WeeklySolutionAvailable() {
    }

    public WeeklySolutionAvailable(Integer weeklySolutionId) {
        this.weeklySolutionId = weeklySolutionId;
    }

    public Integer getWeeklySolutionId() {
        return weeklySolutionId;
    }

    @Transient
    /**
     * Return a Human digestable message about this event. Used in email for example.
     * 
     * @return the message
     */
    public String getMessage() {
        String message = super.getMessage() + ", WEEKLY_SOLUTION_ID: " + getWeeklySolutionId();
        return message;
    }
}
