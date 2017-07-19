package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/humiditySensor.xsd:humiditySensorType
 */
@Entity
@Table(name = "SITELOG_HUMIDITYSENSOR")
public class HumiditySensorLogItem extends SensorEquipmentLogItem<HumiditySensorLogItem> {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGHUMIDITYSENSOR")
    private Integer id;

    @Size(max = 256)
    @Column(name = "DATA_SAMPLING_INTERVAL", length = 256)
    protected String dataSamplingInterval;

    @Size(max = 256)
    @Column(name = "ACCURACY_PERCENT_REL_HUMIDITY", length = 256)
    protected String accuracyPercentRelativeHumidity;

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
     * Return accuracy percent relative humidity.
     */
    public String getAccuracyPercentRelativeHumidity() {
        return accuracyPercentRelativeHumidity;
    }

    /**
     * Set accuracy percent relative humidity.
     */
    public void setAccuracyPercentRelativeHumidity(String value) {
        this.accuracyPercentRelativeHumidity = value;
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
