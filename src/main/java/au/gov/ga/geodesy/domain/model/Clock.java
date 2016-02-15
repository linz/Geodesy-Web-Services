package au.gov.ga.geodesy.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;

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

    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }
        if (x == this) {
            return true;
        }
        if (x.getClass() != getClass()) {
            return false;
        }
        Clock other = (Clock) x;
        return new EqualsBuilder()
        .append(getType(), other.getType())
            .isEquals();
    }
}
