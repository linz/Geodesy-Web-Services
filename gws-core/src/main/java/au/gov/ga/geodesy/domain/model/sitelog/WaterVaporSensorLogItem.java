package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2004/waterVaporSensor.xsd:waterVaporSensor
 */
@Entity
@Table(name = "SITELOG_WATERVAPORSENSOR")
public class WaterVaporSensorLogItem extends SensorEquipmentLogItem<WaterVaporSensorLogItem> {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGWATERVAPORSENSOR")
    private Integer id;

    @Column(name = "DISTANCE_TO_ANTENNA")
    protected Double distanceToAntenna;

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
     * Return distance to antenna.
     */
    public Double getDistanceToAntenna() {
        return distanceToAntenna;
    }

    /**
     * Set distance to antenna.
     */
    public void setDistanceToAntenna(Double value) {
        this.distanceToAntenna = value;
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
