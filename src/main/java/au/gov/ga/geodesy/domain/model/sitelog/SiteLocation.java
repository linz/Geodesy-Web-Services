package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/monumentInfo/2564/siteLocation.xsd:siteLocationType
 */
@Embeddable
public class SiteLocation {

    @Size(max = 256)
    @Column(name = "city", length = 256)
    protected String city;

    @Size(max = 256)
    @Column(name = "STATE", length = 256)
    protected String state;

    @Size(max = 256)
    @Column(name = "COUNTRY", length = 256)
    protected String country;

    @Size(max = 256)
    @Column(name = "TECTONIC_PLATE", length = 256)
    protected String tectonicPlate;

    @Valid
    @Embedded
    protected ApproximatePosition approximatePosition;

    @Size(max = 4000)
    @Column(name = "LOCATION_NOTES", length = 4000)
    protected String notes;

    /**
     * Return city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Set city.
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Return state.
     */
    public String getState() {
        return state;
    }

    /**
     * Set state.
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Return country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set country.
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Return tectonic plate.
     */
    public String getTectonicPlate() {
        return tectonicPlate;
    }

    /**
     * Set tectonic plate.
     */
    public void setTectonicPlate(String value) {
        this.tectonicPlate = value;
    }

    /**
     * Return approximate position (ITRF).
     */
    public ApproximatePosition getApproximatePosition() {
        return approximatePosition;
    }

    /**
     * Set approximate position (ITRF).
     */
    public void setApproximatePosition(ApproximatePosition value) {
        this.approximatePosition = value;
    }

    /**
     * Return notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set notes.
     */
    public void setNotes(String value) {
        this.notes = value;
    }

}
