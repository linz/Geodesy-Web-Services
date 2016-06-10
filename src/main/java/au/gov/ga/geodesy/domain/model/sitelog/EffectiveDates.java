package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2004/baseEquipmentLib.xsd:baseSensorEquipmentType.effectiveDates Note: this is an attempt to
 * interpret date values as java dates, rather than as strings as defined in the SOPAC schema (feel free to revert to java strings, if this starts to cause
 * trouble).
 */
@Embeddable
public class EffectiveDates {

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
        setFrom(from);
        setTo(to);
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() { return to; }

    public void setTo(Instant to) {
        this.to = to;
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
        EffectiveDates other = (EffectiveDates) x;
        if (from == null && to == null) {
            return other.getFrom() == null && other.getTo() == null;
        }
        if (from == null) {
            return other.getFrom() == null && other.getTo().equals(to);
        }
        if (to == null) {
            return other.getTo() == null && other.getFrom().equals(from);
        }
        return from.equals(other.getFrom()) && to.equals(other.getTo());
    }

    /**
     * Implement compareTo method so that collections of EffectiveDate objects can be sorted properly
     * @param other the EffectiveDate to compare with this one
     * @return int value of comparison
     */
    public int compareTo(EffectiveDates other) {
        int result = 0;
        if (other == null) {
            result = 1;
        } else {
            if (from != null && other.getFrom() != null) {
                result += from.compareTo(other.getFrom());
            }
            if (result == 0 && to != null && other.getTo() != null) {
                result += to.compareTo(other.getTo());
            }
        }
        return result;
    }
}
