package au.gov.ga.geodesy.domain.model;

import java.time.Instant;

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
    private Instant asAt;

    @Column(name = "EPOCH")
    private Instant epoch;

    @Column(name = "SINEX_FILE_NAME")
    private String sinexFile;

    @SuppressWarnings("unused") // used by hibernate
    private WeeklySolution() {
    }

    public WeeklySolution(Instant asAt, Instant epoch, String sinexFile) {
        setAsAt(asAt);
        setEpoch(epoch);
        setSinexFile(sinexFile);
    }

    public Integer getId() {
        return id;
    }

    public Instant getAsAt() {
        return asAt;
    }

    private void setAsAt(Instant asAt) {
        this.asAt = asAt;
    }

    public Instant getEpoch() {
        return epoch;
    }

    private void setEpoch(Instant epoch) {
        this.epoch = epoch;
    }

    public String getSinexFile() {
        return sinexFile;
    }

    private void setSinexFile(String sinexFile) {
        this.sinexFile = sinexFile;
    }
}