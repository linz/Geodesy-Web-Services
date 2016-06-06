package au.gov.ga.geodesy.domain.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "POSITION")
public class Position {

    /**
     * RDBMS surrogate key
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "seq_surrogate_keys")
    private Integer id;

    @Column(name = "FOUR_CHARACTER_ID")
    private String fourCharacterId;

    @Column(name = "NODE_ID")
    private Integer nodeId;

    @Column(name = "DATUM_EPSG_CODE")
    private Integer datumEpsgCode;

    @Column(name = "X")
    private Double x;

    @Column(name = "Y")
    private Double y;

    @Column(name = "EPOCH")
    private Instant epoch;

    @Column(name = "AS_AT")
    private Instant asAt;

    @Column(name = "POSITION_SOURCE_ID")
    private Integer positionSourceId;

    @SuppressWarnings("unused") // used by hibernate
    private Position() {
    }

    public Position(String fourCharacterId, Integer nodeId, Integer datumEpsgCode, Double x, Double y,
            Instant epoch, Instant asAt, Integer positionSourceId) {
        setFourCharacterId(fourCharacterId);
        setNodeId(nodeId);
        setDatumEpsgCode(datumEpsgCode);
        setX(x);
        setY(y);
        setEpoch(epoch);
        setAsAt(asAt);
        setPositionSourceId(positionSourceId);
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }

    public void setFourCharacterId(String id) {
        this.fourCharacterId = id;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getDatumEpsgCode() {
        return datumEpsgCode;
    }

    public void setDatumEpsgCode(Integer datumEpsgCode) {
        this.datumEpsgCode = datumEpsgCode;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Instant getEpoch() {
        return epoch;
    }

    public void setEpoch(Instant epoch) {
        this.epoch = epoch;
    }

    public Instant getAsAt() {
        return asAt;
    }

    public void setAsAt(Instant asAt) {
        this.asAt = asAt;
    }

    public Integer getPositionSourceId() {
        return positionSourceId;
    }

    public void setPositionSourceId(Integer positionSourceId) {
        this.positionSourceId = positionSourceId;
    }
}
