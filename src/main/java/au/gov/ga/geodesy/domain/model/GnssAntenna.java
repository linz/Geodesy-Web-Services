package au.gov.ga.geodesy.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "geodesy.GnssAntenna")
@Table(name = "GNSS_ANTENNA")
@DiscriminatorValue("GNSS antenna")
public class GnssAntenna extends Equipment {

    private GnssAntenna() {
        super(null, null);
    }

    public GnssAntenna(String type, String serialNumber) {
        super(type, serialNumber);
    }
}
