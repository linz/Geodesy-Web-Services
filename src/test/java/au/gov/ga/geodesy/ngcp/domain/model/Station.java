package au.gov.ga.geodesy.ngcp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * All Station entities in the old NGCP database.
 */
@Entity
@Table(name = "STATIONS", schema = "NGCP")
@Inheritance(strategy = InheritanceType.JOINED)
public class Station {

    @Id
    @Column(name = "STATIONNO")
    private Integer id;

    @Column(name = "GPSSITEID", nullable = false)
    private String gpsSiteId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer stationno) {
        this.id = stationno;
    }

    public String getGpsSiteId() {
        return gpsSiteId;
    }

    public void setGpsSiteId(String gpsSiteId) {
        this.gpsSiteId = gpsSiteId;
    }
}
