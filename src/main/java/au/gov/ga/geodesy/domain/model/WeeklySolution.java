package au.gov.ga.geodesy.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "WEEKLY_SOLUTION")
public class WeeklySolution {

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @Column(name = "epoch")
    private Date epoch;

    @Column(name = "sinexFile")
    private String sinexFile;

    @SuppressWarnings("unused") // used by hibernate
    private WeeklySolution() {
    }

    public WeeklySolution(Date epoch, String sinexFile) {
        setEpoch(epoch);
        setSinexFile(sinexFile);
    }

    public Date getEpoch() {
        return epoch;
    }

    public void setEpoch(Date epoch) {
        this.epoch = epoch;
    }

    public String getSinexFile() {
        return sinexFile;
    }

    public void setSinexFile(String sinexFile) {
        this.sinexFile = sinexFile;
    }
}
