package au.gov.ga.geodesy.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GNSS_CORS_SITE")
public class GnssCorsSite extends Site {

    /**
     * Business id
     */
    @Column(name = "FOUR_CHARACTER_ID", length = 4, nullable = false, unique = true)
    private String fourCharacterId;

    @Column(name = "DOMES_NUMBER")
    private String domesNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MONUMENT_ID")
    private Monument monument;

    @Column(name = "GEOLOGIC_CHARACTERISTIC")
    private String geologicCharacteristic;

    @Column(name = "BEDROCK_TYPE")
    private String bedrockType;

    @Column(name = "BEDROCK_CONDITION")
    private String bedrockCondition;

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

    public Monument getMonument() {
        return monument;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }

    public String getGeologicCharacteristic() {
        return geologicCharacteristic;
    }

    public void setGeologicCharacteristic(String geologicCharacteristic) {
        this.geologicCharacteristic = geologicCharacteristic;
    }

    public String getBedrockType() {
        return bedrockType;
    }

    public void setBedrockType(String bedrockType) {
        this.bedrockType = bedrockType;
    }
}
