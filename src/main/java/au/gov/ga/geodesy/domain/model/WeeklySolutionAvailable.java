package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

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
}
