package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "geodesy.GnssReceiver")
@Table(name = "GNSS_RECEIVER")
@DiscriminatorValue("gnss receiver")
public class GnssReceiver extends Equipment {

    @Column(name = "TYPE", nullable = false)
    private String type;

    private GnssReceiver() {
        super(null);
    }

    public GnssReceiver(String serialNumber, String type) {
        super(serialNumber);
        setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
