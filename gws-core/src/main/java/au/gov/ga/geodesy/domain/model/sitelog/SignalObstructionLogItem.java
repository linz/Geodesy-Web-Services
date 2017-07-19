package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/localInterferences/2004/baseLocalInterferencesLib.xsd:basePossibleProblemSourcesType
 */
@Entity
@Table(name = "SITELOG_SIGNALOBSTRACTION")
public class SignalObstructionLogItem extends PossibleProblemSourceLogItem<SignalObstructionLogItem> {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGSIGNALOBSTRUCTION")
    private Integer id;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }
}
