package au.gov.ga.geodesy.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("clock")
public class Clock extends Equipment {

    @SuppressWarnings("unused") // used by hibernate
    private Clock() {
        this(null);
    }

    public Clock(String type) {
        super(type);
    }
}
