package au.gov.ga.geodesy.domain.model.equipment;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Entity
@Table(name = "EQUIPMENT")
@DiscriminatorColumn(name = "EQUIPMENT_TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// TODO: lbodor, make abstract
public class Equipment {

    @Version
    private @MonotonicNonNull Integer version;

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    protected @MonotonicNonNull Integer id;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "EQUIPMENT_TYPE", insertable = false, updatable = false)
    private @MonotonicNonNull String equipmentType;

    @Column(name = "MANUFACTURER")
    private @MonotonicNonNull String manufacturer;

    @Column(name = "SERIAL_NUMBER")
    private @MonotonicNonNull String serialNumber;

    @SuppressWarnings({"unused", "initialization.fields.uninitialized"}) // used by hibernate
    private Equipment() {
    }

    public Equipment(String type) {
        this.type = type;
    }

    public Equipment(String type, String serialNumber) {
        this(type);
        this.serialNumber = serialNumber;
    }

    public @Nullable Integer getVersion() {
        return version;
    }

    public @Nullable Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    // TODO: lbodor, remove
    public @Nullable String getEquipmentType() {
        return equipmentType;
    }

    public @Nullable String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public @Nullable String getSerialNumber() {
        return serialNumber;
    }
}
