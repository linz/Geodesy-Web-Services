package au.gov.ga.geodesy.ngcp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * All GNSS Stations in the old NGCP database.
 */
@Entity
@Table(name = "GNSS_STATIONS", schema = "NGCP")
public class GnssStation extends Station {

    @Column(name = "IGSID", nullable = false)
    private String igsId;

    protected String getIgsId() {
        return igsId;
    }

    protected void setIgsId(String igsId) {
        this.igsId = igsId;
    }
}
