package au.gov.ga.geodesy.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Entity
@Table(name = "CORS_SITE")
public class CorsSite extends Site {

    /**
     * Business id
     */
    @Column(name = "FOUR_CHARACTER_ID", length = 4, nullable = false, unique = true)
    private String fourCharacterId;

    @Column(name = "DOMES_NUMBER")
    private @MonotonicNonNull String domesNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MONUMENT_ID", foreignKey = @ForeignKey(name="FK_CORS_SITE_MONUMENT"))
    private @MonotonicNonNull Monument monument;

    @Column(name = "GEOLOGIC_CHARACTERISTIC")
    private @MonotonicNonNull String geologicCharacteristic;

    @Column(name = "BEDROCK_TYPE")
    private @MonotonicNonNull String bedrockType;

    @Column(name = "BEDROCK_CONDITION")
    private @MonotonicNonNull String bedrockCondition;

    @Column(name = "SITE_STATUS")
    private String siteStatus = "PUBLIC";

    @SuppressWarnings({"unused", "initialization.fields.uninitialized"}) // hibernate needs the default constructor
    private CorsSite() {
    }

    public CorsSite(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }

    public @Nullable String getDomesNumber() {
        return domesNumber;
    }

    public void setDomesNumber(String domesNumber) {
        this.domesNumber = domesNumber;
    }

    public @Nullable Monument getMonument() {
        return monument;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }

    public @Nullable String getGeologicCharacteristic() {
        return geologicCharacteristic;
    }

    public void setGeologicCharacteristic(String geologicCharacteristic) {
        this.geologicCharacteristic = geologicCharacteristic;
    }

    public @Nullable String getBedrockType() {
        return bedrockType;
    }

    public void setBedrockType(String bedrockType) {
        this.bedrockType = bedrockType;
    }

    public String getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }
}
