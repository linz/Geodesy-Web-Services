package au.gov.ga.geodesy.domain.model.equipment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
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
        // TODO: would delegating to super be enough
        .append(getType(), other.getType())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(21, 43)
            .appendSuper(super.hashCode())
            // TODO: would delegating to super be enough
            .append(getType())
            .toHashCode();
   }
}
