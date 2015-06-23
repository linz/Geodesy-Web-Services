package au.gov.ga.geodesy.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "geodesy.GnssReceiver")
@Table(name = "GNSS_RECEIVER")
@DiscriminatorValue("GNSS Receiver")
public class GnssReceiver extends Equipment {

    private GnssReceiver() {
        super(null, null);
    }

    public GnssReceiver(String type, String serialNumber) {
        super(type, serialNumber);
    }
}
