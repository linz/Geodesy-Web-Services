package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/temperatureSensor.xsd:temperatureSensorType
 */
@Entity
@Table(name = "SITELOG_TEMPERATURESENSOR")
public class TemperatureSensorLogItem extends SensorEquipmentLogItem {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGTEMPERATURESENSOR")
    private Integer id;

    @Size(max = 256)
    @Column(name = "DATA_SAMPLING_INTERVAL", length = 256)
    protected String dataSamplingInterval;

    @Column(name = "ACCURACE_DEGREE_CELCIUS", length = 256)
    protected String accuracyDegreesCelcius;

    @Size(max = 256)
    @Column(name = "ASPIRATION", length = 256)
    protected String aspiration;

    @Size(max = 4000)
    @Column(name = "NOTES", length = 4000)
    protected String notes;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Return data sampling interval.
     */
    public String getDataSamplingInterval() {
        return dataSamplingInterval;
    }

    /**
     * Set data sampling interval.
     */
    public void setDataSamplingInterval(String value) {
        this.dataSamplingInterval = value;
    }

    /**
     * Return accuracy degrees ceclius.
     */
    public String getAccuracyDegreesCelcius() {
        return accuracyDegreesCelcius;
    }

    /**
     * Set accuracy degrees celcius.
     */
    public void setAccuracyDegreesCelcius(String value) {
        this.accuracyDegreesCelcius = value;
    }

    /**
     * Return aspiration.
     */
    public String getAspiration() {
        return aspiration;
    }

    /**
     * Set aspiration.
     */
    public void setAspiration(String value) {
        this.aspiration = value;
    }

    /**
     * Return notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set notes.
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
