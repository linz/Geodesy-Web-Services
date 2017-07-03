package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Past;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

@MappedSuperclass
public abstract class LogItem {

    @Past
    @Column(name = "DATE_INSERTED")
    protected Instant dateInserted;

    @Past
    @Column(name = "DATE_DELETED")
    protected @MonotonicNonNull Instant dateDeleted;

    @Past
    @Column(name = "DELETED_REASON")
    protected @MonotonicNonNull String deletedReason;

    public abstract EffectiveDates getEffectiveDates();

    public abstract <T> T accept(LogItemVisitor<T> v);

    public Instant getDateInserted() {
        return dateInserted;
    }

    public void setDateInserted(Instant dateInserted) {
        this.dateInserted = dateInserted;
    }

    public Instant getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Instant dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public String getDeletedReason() {
        return deletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }
}

