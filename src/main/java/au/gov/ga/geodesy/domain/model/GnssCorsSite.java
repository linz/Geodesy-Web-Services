package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gnss_cors_site")
public class GnssCorsSite extends Site {

    /**
     * Business id
     */
    @Column(name = "four_character_id", length = 4, nullable = false, unique = true)
    private String fourCharacterId;

    @SuppressWarnings("unused") // hibernate needs the default constructor
    private GnssCorsSite() {
    }

    public GnssCorsSite(String fourCharacterId) {
        super();
        /* gnssCorsSiteId = id; */
        setFourCharacterId(fourCharacterId);
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }

    private void setFourCharacterId(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }
}
