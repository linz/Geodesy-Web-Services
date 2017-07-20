package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/localInterferences/2004/radioInterferences.xsd:radioInterferencesType
 */
@Entity
@Table(name = "SITELOG_RADIOINTERFERENCE")
public class RadioInterferenceLogItem extends PossibleProblemSourceLogItem<RadioInterferenceLogItem> {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGRADIOINTERFERENCE")
    private Integer id;

    @Size(max = 256)
    @Column(name = "OBSERVED_DEGRADATION", length = 256)
    protected String observedDegradation;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Return observed degradation.
     */
    public String getObservedDegradation() {
        return observedDegradation;
    }

    /**
     * Set observed degradation.
     */
    public void setObservedDegradation(String value) {
        this.observedDegradation = value;
    }
}
