package au.gov.ga.geodesy.domain.model;

import java.util.Date;

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

    @Column(name = "SITE_ID")
    private Integer siteId;

    @Column(name = "NODE_ID")
    private Integer nodeId;

    @Column(name = "DATUM_EPSG_CODE")
    private Integer datumEpsgCode;

    @Column(name = "X")
    private Double x;

    @Column(name = "Y")
    private Double y;

    @Column(name = "EPOCH")
    private Date epoch;

    @Column(name = "AS_AT")
    private Date asAt;

    @Column(name = "POSITION_SOURCE_ID")
    private Integer positionSourceId;

    @SuppressWarnings("unused") // used by hibernate
    private Position() {
    }

    public Position(Integer siteId, Integer nodeId, Integer datumEpsgCode, Double x, Double y, Date epoch, Date asAt,
            Integer positionSourceId) {

        setSiteId(siteId);
        setNodeId(nodeId);
        setDatumEpsgCode(datumEpsgCode);
        setX(x);
        setY(y);
        setEpoch(epoch);
        setAsAt(asAt);
        setPositionSourceId(positionSourceId);
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
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

    public Date getEpoch() {
        return epoch;
    }

    public void setEpoch(Date epoch) {
        this.epoch = epoch;
    }

    public Date getAsAt() {
        return asAt;
    }

    public void setAsAt(Date asAt) {
        this.asAt = asAt;
    }

    public Integer getPositionSourceId() {
        return positionSourceId;
    }

    public void setPositionSourceId(Integer positionSourceId) {
        this.positionSourceId = positionSourceId;
    }
}
