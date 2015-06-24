package au.gov.ga.geodesy.domain.model;

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

@Entity
@Table(name = "EQUIPMENT")
@DiscriminatorColumn(name = "EQUIPMENT_TYPE")
@Inheritance(strategy = InheritanceType.JOINED)
/* @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "equipmentType") */
public class Equipment {

    @Version
    private Integer version;

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    /* @Type(type = "au.gov.ga.geodesy.support.hibernate.EntityIdUserType") */
    protected Integer id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "EQUIPMENT_TYPE", insertable = false, updatable = false)
    private String equipmentType;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Column(name = "SERIAL_NUMBER", nullable = true)
    private String serialNumber;

    @SuppressWarnings("unused") // used by hibernate
    private Equipment() {
    }

    public Equipment(String type, String serialNumber) {
        setType(type);
        setSerialNumber(serialNumber);
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
