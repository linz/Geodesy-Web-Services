package au.gov.ga.geodesy.domain.model.sitelog;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Past;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

@MappedSuperclass
public abstract class LogItem<L extends LogItem<L>> implements Comparable<L> {

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

    @Override
    public int compareTo(L x) {
        int byDate = new CompareToBuilder()
            .append(this.getEffectiveDates(), x.getEffectiveDates())
            .toComparison();

        if (byDate != 0) {
            return byDate;
        }
        Field[] fields = this.getClass().getDeclaredFields();
        Arrays.sort(fields, new Comparator<Field>() {
            public int compare(Field f, Field g) {
                return f.getName().compareTo(g.getName());
            }
        });
        AccessibleObject.setAccessible(fields, true);

        CompareToBuilder builder = new CompareToBuilder();
        for (Field field : fields) {
            if (field.getName().equals("id")) {
                continue;
            }
            try {
                if (Comparable.class.isAssignableFrom(field.getType())) {
                    builder.append(field.get(this), field.get(x));
                }
            } catch (IllegalAccessException ok) {
            }
        }
        return builder.toComparison();
    }
}

