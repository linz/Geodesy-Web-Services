package au.gov.ga.geodesy.domain.model.sitelog;

import java.util.Date;

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
    private Date from;

    @Column(name = "EFFECTIVE_TO")
    private Date to;

    public EffectiveDates() {
    }

    public EffectiveDates(Date from) {
        this(from, null);
    }

    public EffectiveDates(Date from, Date to) {
        setFrom(from);
        setTo(to);
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
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
        return equals(from, other.getFrom()) && equals(to, other.getTo());
    }

    /**
     * We compare dates ourselves because some are java.util.Dates and some are
     * java.sql.Timestamps.
     */
    private boolean equals(Date a, Date b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.getTime() == b.getTime();
    }
}
