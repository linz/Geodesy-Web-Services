package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "geodesy.HumiditySensor")
@Table(name = "HUMIDITY_SENSOR")
@DiscriminatorValue("humidity sensor")
public class HumiditySensor extends Equipment {

    @Column(name = "ASPIRATION")
    private String aspiration;

    private HumiditySensor() {
        super(null, null);
    }

    public HumiditySensor(String type, String serialNumber) {
        super(type, serialNumber);
    }

    public String getAspiration() {
        return aspiration;
    }

    public void setAspiration(String aspiration) {
        this.aspiration = aspiration;
    }
}


