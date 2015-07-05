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

    @Column(name = "AS_AT")
    private Date asAt;

    @Column(name = "EPOCH")
    private Date epoch;

    @Column(name = "SINEX_FILE_NAME")
    private String sinexFile;

    @SuppressWarnings("unused") // used by hibernate
    private WeeklySolution() {
    }

    public WeeklySolution(Date asAt, Date epoch, String sinexFile) {
        setAsAt(asAt);
        setEpoch(epoch);
        setSinexFile(sinexFile);
    }

    public Integer getId() {
        return id;
    }

    public Date getAsAt() {
        return asAt;
    }

    private void setAsAt(Date asAt) {
        this.asAt = asAt;
    }

    public Date getEpoch() {
        return epoch;
    }

    private void setEpoch(Date epoch) {
        this.epoch = epoch;
    }

    public String getSinexFile() {
        return sinexFile;
    }

    private void setSinexFile(String sinexFile) {
        this.sinexFile = sinexFile;
    }
}
