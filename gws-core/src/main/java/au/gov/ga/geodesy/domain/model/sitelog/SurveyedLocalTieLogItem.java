package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/surveyedLocalTies.xsd:surveyedLocalTiesType
 */
@Entity
@Table(name = "SITELOG_SURVEYEDLOCALTIE")
public class SurveyedLocalTieLogItem extends LogItem {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGLOCALTIE")
    private Integer id;

    @Size(max = 256)
    @Column(name = "TIED_MARKER_NAME", length = 256)
    protected String tiedMarkerName;

    @Size(max = 256)
    @Column(name = "TIED_MARKER_USAGE", length = 256)
    protected String tiedMarkerUsage;

    @Size(max = 256)
    @Column(name = "TIED_MARKER_CDP_NUMBER", length = 256)
    protected String tiedMarkerCdpNumber;

    @Size(max = 256)
    @Column(name = "TIED_MARKER_DOMES_NUMBER", length = 256)
    protected String tiedMarkerDomesNumber;

    @Valid
    @Embedded
    protected DifferentialFromMarker differentialFromMarker;

    @Size(max = 256)
    @Column(name = "LOCAL_SITE_TIE_ACCURACY", length = 256)
    protected String localSiteTieAccuracy;

    @Size(max = 256)
    @Column(name = "SURVEY_METHOD", length = 256)
    protected String surveyMethod;

    @Past
    @Column(name = "DATE_MEASURED")
    protected Instant dateMeasured;

    @Size(max = 4000)
    @Column(name = "NOTES", length = 4000)
    protected String notes;

    @SuppressWarnings("unused")
    private Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Return tied marker name.
     */
    public String getTiedMarkerName() {
        return tiedMarkerName;
    }

    /**
     * Set tied marker name.
     */
    public void setTiedMarkerName(String value) {
        this.tiedMarkerName = value;
    }

    /**
     * Return tied marker usage.
     */
    public String getTiedMarkerUsage() {
        return tiedMarkerUsage;
    }

    /**
     * Set tied marker usage.
     */
    public void setTiedMarkerUsage(String value) {
        this.tiedMarkerUsage = value;
    }

    /**
     * Return tied marker CDP number.
     */
    public String getTiedMarkerCdpNumber() {
        return tiedMarkerCdpNumber;
    }

    /**
     * Set tied marker CDP number.
     */
    public void setTiedMarkerCdpNumber(String value) {
        this.tiedMarkerCdpNumber = value;
    }

    /**
     * Return tied marker DOMES number.
     */
    public String getTiedMarkerDomesNumber() {
        return tiedMarkerDomesNumber;
    }

    /**
     * Set tied marker DOMES number.
     */
    public void setTiedMarkerDomesNumber(String value) {
        this.tiedMarkerDomesNumber = value;
    }

    /**
     * Return differential from GNSS marker to tied monument.
     */
    public DifferentialFromMarker getDifferentialFromMarker() {
        return differentialFromMarker;
    }

    /**
     * Set diffential from GNSS marker to tied monument.
     */
    public void setDifferentialFromMarker(DifferentialFromMarker value) {
        this.differentialFromMarker = value;
    }

    /**
     * Return tie accuracy in millimetres.
     */
    public String getLocalSiteTieAccuracy() {
        return localSiteTieAccuracy;
    }

    /**
     * Set tie accuracy (millimeters).
     */
    public void setLocalSiteTieAccuracy(String value) {
        this.localSiteTieAccuracy = value;
    }

    /**
     * Set survey method.
     */
    public String getSurveyMethod() {
        return surveyMethod;
    }

    /**
     * Set survey method.
     */
    public void setSurveyMethod(String value) {
        this.surveyMethod = value;
    }

    /**
     * Return date measured.
     */
    public Instant getDateMeasured() {
        return dateMeasured;
    }

    /**
     * Set date measured.
     */
    public void setDateMeasured(Instant value) {
        this.dateMeasured = value;
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

    public EffectiveDates getEffectiveDates() {
        return new EffectiveDates(this.getDateMeasured(), this.getDateMeasured());
    }

    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
