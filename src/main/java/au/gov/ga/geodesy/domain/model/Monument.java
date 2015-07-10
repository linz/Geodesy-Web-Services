package au.gov.ga.geodesy.domain.model; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MONUMENT")
public class Monument {

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    protected Integer id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FOUNDATION")
    private String foundation;

    @Column(name = "MARKER_DESCRIPTION")
    private String markerDescription;

    @Column(name = "HEIGHT")
    private String height;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoundation() {
        return foundation;
    }

    public void setFoundation(String foundation) {
        this.foundation = foundation;
    }

    public String getMarkerDescription() {
        return markerDescription;
    }

    public void setMarkerDescription(String markerDescription) {
        this.markerDescription = markerDescription;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
