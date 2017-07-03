package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2564/antenna.xsd:gnssAntennaType
 */
@Entity
@Table(name = "SITELOG_GNSSANTENNA")
public class GnssAntennaLogItem extends EquipmentLogItem {

    @Id
    @GeneratedValue(generator = "surrogateKeyGenerator")
    @SequenceGenerator(name = "surrogateKeyGenerator", sequenceName = "SEQ_SITELOGANTENNA")
    private Integer id;

    @Size(max = 256)
    @Column(name = "ANTENNA_TYPE", length = 256)
    protected String type;

    @Size(max = 256)
    @Column(name = "SERIAL_NUMBER", length = 256)
    protected String serialNumber;

    @Size(max = 256)
    @Column(name = "ANTENNA_REFERENCE_POINT", length = 256)
    protected String antennaReferencePoint;

    @Column(name = "MARKER_ARP_UP_ECC")
    protected Double markerArpUpEcc;

    @Column(name = "MARKER_ARP_NORTH_ECC")
    protected Double markerArpNorthEcc;

    @Column(name = "MARKER_ARP_EAST_ECC")
    protected Double markerArpEastEcc;

    @Size(max = 256)
    @Column(name = "ALIGNMENT_FROM_TRUE_NORTH", length = 256)
    protected String alignmentFromTrueNorth;

    @Size(max = 256)
    @Column(name = "ANTENNA_RADOME_TYPE", length = 256)
    protected String antennaRadomeType;

    @Size(max = 256)
    @Column(name = "RADOME_SERIAL_NUMBER", length = 256)
    protected String radomeSerialNumber;

    @Size(max = 256)
    @Column(name = "ANTENNA_CABLE_TYPE", length = 256)
    protected String antennaCableType;

    @Size(max = 256)
    @Column(name = "ANTENNA_CABLE_LENGTH", length = 256)
    protected String antennaCableLength;

    @Past
    @Column(name = "DATE_INSTALLED")
    protected Instant dateInstalled;

    @Past
    @Column(name = "DATE_REMOVED")
    protected Instant dateRemoved;

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
     * Return antenna type.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Set antenna type.
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Return serial number.
     */
    @Override
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Set serial number.
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

    /**
     * Return antenna reference point.
     */
    public String getAntennaReferencePoint() {
        return antennaReferencePoint;
    }

    /**
     * Set antenna reference point.
     */
    public void setAntennaReferencePoint(String value) {
        this.antennaReferencePoint = value;
    }

    /**
     * Return marker ARP up ecc.
     */
    public Double getMarkerArpUpEcc() {
        return markerArpUpEcc;
    }

    /**
     * Set marker ARP up ecc.
     */
    public void setMarkerArpUpEcc(Double value) {
        this.markerArpUpEcc = value;
    }

    /**
     * Return marker ARP north ecc.
     */
    public Double getMarkerArpNorthEcc() {
        return markerArpNorthEcc;
    }

    /**
     * Set marker ARP north acc.
     */
    public void setMarkerArpNorthEcc(Double value) {
        this.markerArpNorthEcc = value;
    }

    /**
     * Return marker ARP east acc.
     */
    public Double getMarkerArpEastEcc() {
        return markerArpEastEcc;
    }

    /**
     * Set marker ARP east ecc.
     */
    public void setMarkerArpEastEcc(Double value) {
        this.markerArpEastEcc = value;
    }

    /**
     * Return alignment from true north.
     */
    public String getAlignmentFromTrueNorth() {
        return alignmentFromTrueNorth;
    }

    /**
     * Set alignment from true north.
     */
    public void setAlignmentFromTrueNorth(String value) {
        this.alignmentFromTrueNorth = value;
    }

    /**
     * Return IGS radome model code.
     */
    public String getAntennaRadomeType() {
        return antennaRadomeType;
    }

    /**
     * Set IGS radome model code.
     */
    public void setAntennaRadomeType(String value) {
        this.antennaRadomeType = value;
    }

    /**
     * Return radome serial number.
     */
    public String getRadomeSerialNumber() {
        return radomeSerialNumber;
    }

    /**
     * Set radome serial number.
     */
    public void setRadomeSerialNumber(String value) {
        this.radomeSerialNumber = value;
    }

    /**
     * Return antenna cable type.
     */
    public String getAntennaCableType() {
        return antennaCableType;
    }

    /**
     * Set antenna cable type.
     */
    public void setAntennaCableType(String value) {
        this.antennaCableType = value;
    }

    /**
     * Return antenna cable length.
     */
    public String getAntennaCableLength() {
        return antennaCableLength;
    }

    /**
     * Set antenna cable length.
     */
    public void setAntennaCableLength(String value) {
        this.antennaCableLength = value;
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
     * Return date removed.
     */
    public Instant getDateRemoved() {
        return dateRemoved;
    }

    /**
     * Set date removed.
     */
    public void setDateRemoved(Instant value) {
        this.dateRemoved = value;
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

    @Override
    public EffectiveDates getEffectiveDates() {
        return new EffectiveDates(getDateInstalled(), getDateRemoved());
    }

    @Override
    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
