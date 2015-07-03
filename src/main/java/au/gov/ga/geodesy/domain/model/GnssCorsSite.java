package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GNSS_CORS_SITE")
public class GnssCorsSite extends Site {

    /**
     * Business id
     */
    @Column(name = "FOUR_CHARACTER_ID", length = 4, nullable = false, unique = true)
    private String fourCharacterId;

    @Column(name = "DOMES_NUMBER", nullable = false, unique = true)
    private String domesNumber;

    @SuppressWarnings("unused") // hibernate needs the default constructor
    private GnssCorsSite() {
    }

    public GnssCorsSite(String fourCharacterId) {
        super();
        setFourCharacterId(fourCharacterId);
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }

    private void setFourCharacterId(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }

    public String getDomesNumber() {
        return domesNumber;
    }

    public void setDomesNumber(String domesNumber) {
        this.domesNumber = domesNumber;
    }
}
