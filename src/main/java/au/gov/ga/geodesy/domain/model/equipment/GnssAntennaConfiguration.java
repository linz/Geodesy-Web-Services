package au.gov.ga.geodesy.domain.model.equipment;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GNSS_ANTENNA_CONFIGURATION")
public class GnssAntennaConfiguration extends EquipmentConfiguration {

    @Column(name = "ANTENNA_REFERENCE_POINT")
    private String antennaReferencePoint;

    @Column(name = "MARKER_ARP_UP_ECCENTRICITY")
    private Double markerArpUpEccentricity;

    @Column(name = "MARKER_ARP_NORTH_ECCENTRICITY")
    private Double markerArpNorthEccentricity;

    @Column(name = "MARKER_ARP_EAST_ECCENTRICITY")
    private Double markerArpEastEccentricity;

    @Column(name = "ALIGNMENT_FROM_TRUE_NORTH")
    private String alignmentFromTrueNorth;

    @Column(name = "RADOME_TYPE")
    private String radomeType;

    @Column(name = "RADOME_SERIAL_NUMBER")
    private String radomSerialNumber;

    @Column(name = "ANTENNA_CABLE_TYPE")
    private String antennaCableType;

    @Column(name = "ANTENNA_CABLE_LENGTH")
    private String antennaCableLength;

    @Column(name = "NOTES", length = 4000)
    private String notes;

    /* @SuppressWarnings("unused") // used by hibernate */
    private GnssAntennaConfiguration() {
        super(null, null);
    }

    public GnssAntennaConfiguration(Integer gnssAntennaId, Instant configurationTime) {
        super(gnssAntennaId, configurationTime);
    }

    public String getAntennaReferencePoint() {
        return antennaReferencePoint;
    }

    public void setAntennaReferencePoint(String antennaReferencePoint) {
        this.antennaReferencePoint = antennaReferencePoint;
    }

    public Double getMarkerArpUpEccentricity() {
        return markerArpUpEccentricity;
    }

    public void setMarkerArpUpEccentricity(Double markerArpUpEccentricity) {
        this.markerArpUpEccentricity = markerArpUpEccentricity;
    }

    public Double getMarkerArpNorthEccentricity() {
        return markerArpNorthEccentricity;
    }

    public void setMarkerArpNorthEccentricity(Double markerArpNorthEccentricity) {
        this.markerArpNorthEccentricity = markerArpNorthEccentricity;
    }

    public Double getMarkerArpEastEccentricity() {
        return markerArpEastEccentricity;
    }

    public void setMarkerArpEastEccentricity(Double markerArpEastEccentricity) {
        this.markerArpEastEccentricity = markerArpEastEccentricity;
    }

    public String getAlignmentFromTrueNorth() {
        return alignmentFromTrueNorth;
    }

    public void setAlignmentFromTrueNorth(String alignmentFromTrueNorth) {
        this.alignmentFromTrueNorth = alignmentFromTrueNorth;
    }

    public String getRadomeType() {
        return radomeType;
    }

    public void setRadomeType(String radomeType) {
        this.radomeType = radomeType;
    }

    public String getRadomSerialNumber() {
        return radomSerialNumber;
    }

    public void setRadomSerialNumber(String radomSerialNumber) {
        this.radomSerialNumber = radomSerialNumber;
    }

    public String getAntennaCableType() {
        return antennaCableType;
    }

    public void setAntennaCableType(String antennaCableType) {
        this.antennaCableType = antennaCableType;
    }

    public String getAntennaCableLength() {
        return antennaCableLength;
    }

    public void setAntennaCableLength(String antennaCableLength) {
        this.antennaCableLength = antennaCableLength;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
