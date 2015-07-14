package au.gov.ga.geodesy.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "geodesy.GnssReceiver")
@DiscriminatorValue("gnss receiver")
public class GnssReceiver extends Equipment {

    private GnssReceiver() {
        super(null, null);
    }

    public GnssReceiver(String type, String serialNumber) {
        super(type, serialNumber);
    }
}
