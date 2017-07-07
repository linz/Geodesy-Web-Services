package au.gov.ga.geodesy.domain.model.sitelog;

import afu.org.apache.commons.lang3.builder.EqualsBuilder;
import afu.org.apache.commons.lang3.builder.HashCodeBuilder;
import afu.org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Instant;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2004/baseEquipmentLib.xsd:baseSensorEquipmentType.effectiveDates Note: this is an attempt to
 * interpret date values as java dates, rather than as strings as defined in the SOPAC schema (feel free to revert to java strings, if this starts to cause
 * trouble).
 */
@Embeddable
public class EffectiveDates implements Comparable {

    @Column(name = "EFFECTIVE_FROM")
    private Instant from;

    @Column(name = "EFFECTIVE_TO")
    private Instant to;

    public EffectiveDates() {
    }

    public EffectiveDates(Instant from) {
        this(from, null);
    }

    public EffectiveDates(Instant from, Instant to) {
        this.from = from;
        this.to = to;
    }

    public Instant getFrom() {
        return from;
    }

    public Instant getTo() { return to; }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.from).append(this.to).toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EffectiveDates)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        EffectiveDates other = (EffectiveDates) object;

        return new EqualsBuilder().append(this.from, other.from).append(this.to, other.to).isEquals();
    }

    /**
     * Implement compareTo method so that collections of EffectiveDate objects can be sorted properly
     * Compares from and to dates, accounting for null values.
     *
     * @param otherObject the EffectiveDate to compare with this one
     * @return int value of comparison
     */
    @Override
    public int compareTo(Object otherObject) {
        int result = 0;
        if (otherObject == null) {
            result = 1;
        }
        if (!(otherObject instanceof EffectiveDates)) {
            result = 1;
        }
        else {
            EffectiveDates other = (EffectiveDates)otherObject;
            if (from == null && other.getFrom() != null) {
                result = 1;
            } else if (from != null && other.getFrom() == null) {
                result = -1;
            } else if (from != null && other.getFrom() != null) {
                result += from.compareTo(other.getFrom());
            }
            if (result == 0) {
                if (to == null && other.getTo() != null) {
                    result = 1;
                } else if (to != null && other.getTo() == null) {
                    result = -1;
                } else if (to != null && other.getTo() != null) {
                    result += to.compareTo(other.getTo());
                }
            }
        }
        return result;
    }

    public boolean inRange(Instant time) {
        if (time == null) {
            return false;
        }
        return (this.getFrom() == null || this.getFrom().compareTo(time) <= 0)
            && (this.getTo() == null || this.getTo().compareTo(time) >= 0);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("From", this.from).append("To", this.to).toString();
    }
}
