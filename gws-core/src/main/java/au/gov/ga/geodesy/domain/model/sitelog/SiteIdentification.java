package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import au.gov.ga.geodesy.support.java.util.ImpreciseTime;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/monumentInfo/2564/siteIdentification.xsd:siteIdentificationType
 */
@Embeddable
public class SiteIdentification {

    @Size(max = 256)
    @Column(name = "SITE_NAME", length = 256)
    protected String siteName;

    @NotNull
    @Size(min = 4, max = 4)
    @Column(name = "FOUR_CHARACTER_ID", length = 4)
    protected String fourCharacterId;
    
    @Size(min = 9, max = 9)
    @Column(name = "NINE_CHARACTER_ID", length = 9)
    protected String nineCharacterId;

    @Size(max = 256)
    @Column(name = "MONUMENT_INSCRIPTION", length = 256)
    protected String monumentInscription;

    @Size(max = 256)
    @Column(name = "IERS_DOMES_NUMBER", length = 256)
    protected String iersDOMESNumber;

    @Size(max = 256)
    @Column(name = "CDP_NUMBER", length = 256)
    protected String cdpNumber;

    @Size(max = 256)
    @Column(name = "MONUMENT_DESCRIPTION", length = 256)
    protected String monumentDescription;

    @Size(max = 256)
    @Column(name = "HEIGHT_OF_MONUMENT", length = 256)
    protected String heightOfMonument;

    @Size(max = 256)
    @Column(name = "MONUMENT_FOUNDATION", length = 256)
    protected String monumentFoundation;

    @Size(max = 256)
    @Column(name = "FOUNDATION_DEPTH", length = 256)
    protected String foundationDepth;

    @Size(max = 256)
    @Column(name = "MARKER_DESCRIPTION", length = 256)
    protected String markerDescription;

    // @Embedded
    // @AttributeOverrides({
    //     @AttributeOverride(name="time",
    //                        column=@Column(name="DATE_INSTALLED")),
    //     @AttributeOverride(name="precision",
    //                        column=@Column(name="DATE_INSTALLED_PERCISION"))
    // })
    @Column(name = "DATE_INSTALLED")
    protected Instant dateInstalled;

    @Size(max = 256)
    @Column(name = "GEOLOGIC_CHARACTERISTIC", length = 256)
    protected String geologicCharacteristic;

    @Size(max = 256)
    @Column(name = "BEDROCK_TYPE", length = 256)
    protected String bedrockType;

    @Size(max = 256)
    @Column(name = "BEDROCK_CONDITION", length = 256)
    protected String bedrockCondition;

    @Size(max = 256)
    @Column(name = "FRACTURE_SPACING", length = 256)
    protected String fractureSpacing;

    @Size(max = 256)
    @Column(name = "FAULT_ZONES_NEARBY", length = 256)
    protected String faultZonesNearby;

    @Size(max = 256)
    @Column(name = "DISTANCE_ACTIVITY", length = 256)
    protected String distanceActivity;

    @Size(max = 4000)
    @Column(name = "NOTES", length = 4000)
    protected String notes;

    /**
     * Return site name.
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * Set site name.
     */
    public void setSiteName(String value) {
        this.siteName = value;
    }

    /**
     * Return four character id.
     */
    public String getFourCharacterId() {
        return fourCharacterId;
    }

    /**
     * Set four character id. Four character IDs are converted and stored in upper case.
     */
    public void setFourCharacterId(String value) {
        this.fourCharacterId = value != null ? value.toUpperCase() : null;
    }
    
    /**
     * Return nine character id.
     */
    public String getNineCharacterId() {
        return nineCharacterId;
    }

    /**
     * Set nine character id. Nine character IDs are converted and stored in upper case.
     */
    public void setNineCharacterId(String value) {
        this.nineCharacterId = value != null ? value.toUpperCase() : null;
    }   
    
    /**
     * Return monument inscription.
     */
    public String getMonumentInscription() {
        return monumentInscription;
    }

    /**
     * Set monument inscription.
     */
    public void setMonumentInscription(String value) {
        this.monumentInscription = value;
    }

    /**
     * Return IERS DOMES number.
     */
    public String getIersDOMESNumber() {
        return iersDOMESNumber;
    }

    /**
     * Set IERS DOMES number.
     */
    public void setIersDOMESNumber(String value) {
        this.iersDOMESNumber = value;
    }

    /**
     * Return CDP number.
     */
    public String getCdpNumber() {
        return cdpNumber;
    }

    /**
     * Set CDP number.
     */
    public void setCdpNumber(String value) {
        this.cdpNumber = value;
    }

    /**
     * Return monument description type.
     */
    public String getMonumentDescription() {
        return monumentDescription;
    }

    /**
     * Set monumnent description type.
     */
    public void setMonumentDescription(String value) {
        this.monumentDescription = value;
    }

    /**
     * Return height of monument.
     */
    public String getHeightOfMonument() {
        return heightOfMonument;
    }

    /**
     * Set height of monument.
     */
    public void setHeightOfMonument(String value) {
        this.heightOfMonument = value;
    }

    /**
     * Return monument foundation.
     */
    public String getMonumentFoundation() {
        return monumentFoundation;
    }

    /**
     * Set monument foundation.
     */
    public void setMonumentFoundation(String value) {
        this.monumentFoundation = value;
    }

    /**
     * Return foundation depth.
     */
    public String getFoundationDepth() {
        return foundationDepth;
    }

    /**
     * Set foundation depth.
     */
    public void setFoundationDepth(String value) {
        this.foundationDepth = value;
    }

    /**
     * Return marker description.
     */
    public String getMarkerDescription() {
        return markerDescription;
    }

    /**
     * Set marker description.
     */
    public void setMarkerDescription(String value) {
        this.markerDescription = value;
    }

    /**
     * Return date installed.
     */
    public Instant getDateInstalled() {
        return dateInstalled;
    }

    /**
     * Set date installed.
     */
    public void setDateInstalled(Instant value) {
        this.dateInstalled = value;
    }

    /**
     * Return geologic characteristic.
     */
    public String getGeologicCharacteristic() {
        return geologicCharacteristic;
    }

    /**
     * Set geologic characteristic.
     */
    public void setGeologicCharacteristic(String value) {
        this.geologicCharacteristic = value;
    }

    /**
     * Return bedrock type.
     */
    public String getBedrockType() {
        return bedrockType;
    }

    /**
     * Set bedrock type.
     */
    public void setBedrockType(String value) {
        this.bedrockType = value;
    }

    /**
     * Return bedrock condition.
     */
    public String getBedrockCondition() {
        return bedrockCondition;
    }

    /**
     * Set bedrock condition.
     */
    public void setBedrockCondition(String value) {
        this.bedrockCondition = value;
    }

    /**
     * Return fracture spacing.
     */
    public String getFractureSpacing() {
        return fractureSpacing;
    }

    /**
     * Set fracture spacing.
     */
    public void setFractureSpacing(String value) {
        this.fractureSpacing = value;
    }

    /**
     * Return nearby fault zones.
     */
    public String getFaultZonesNearby() {
        return faultZonesNearby;
    }

    /**
     * Set nearby fault zones.
     */
    public void setFaultZonesNearby(String value) {
        this.faultZonesNearby = value;
    }

    /**
     * Return distance activity.
     */
    public String getDistanceActivity() {
        return distanceActivity;
    }

    /**
     * Set distance activity.
     */
    public void setDistanceActivity(String value) {
        this.distanceActivity = value;
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
