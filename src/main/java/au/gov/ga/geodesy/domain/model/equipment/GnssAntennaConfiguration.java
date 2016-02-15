package au.gov.ga.geodesy.domain.model.equipment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GNSS_ANTENNA_CONFIGURATION")
public class GnssAntennaConfiguration extends EquipmentConfiguration {

    @Column(name = "ANTENNA_REFERENCE_POINT")
    private String antennaReferencePoint;

    @Column(name = "MARKER_ARP_UP_ECCENTRICITY")
    private String markerArpUpEccentricity;

    @Column(name = "MARKER_ARP_NORTH_ECCENTRICITY")
    private String markerArpNorthEccentricity;

    @Column(name = "MARKER_ARP_EAST_ECCENTRICITY")
    private String markerArpEastEccentricity;

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

    public GnssAntennaConfiguration(Integer gnssAntennaId, Date configurationTime) {
        super(gnssAntennaId, configurationTime);
    }

    public String getAntennaReferencePoint() {
        return antennaReferencePoint;
    }

    public void setAntennaReferencePoint(String antennaReferencePoint) {
        this.antennaReferencePoint = antennaReferencePoint;
    }

    public String getMarkerArpUpEccentricity() {
        return markerArpUpEccentricity;
    }

    public void setMarkerArpUpEccentricity(String markerArpUpEccentricity) {
        this.markerArpUpEccentricity = markerArpUpEccentricity;
    }

    public String getMarkerArpNorthEccentricity() {
        return markerArpNorthEccentricity;
    }

    public void setMarkerArpNorthEccentricity(String markerArpNorthEccentricity) {
        this.markerArpNorthEccentricity = markerArpNorthEccentricity;
    }

    public String getMarkerArpEastEccentricity() {
        return markerArpEastEccentricity;
    }

    public void setMarkerArpEastEccentricity(String markerArpEastEccentricity) {
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
