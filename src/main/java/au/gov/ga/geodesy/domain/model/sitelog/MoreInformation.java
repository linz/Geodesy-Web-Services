package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/monumentInfo/2564/moreInformation.xsd:moreInformationType
 */
public class MoreInformation {

    @Size(max = 256)
    @Column(name = "MI_PRIMARY_DATA_CENTER", length = 256)
    protected String primaryDataCenter;

    @Size(max = 256)
    @Column(name = "MI_SECONDARY_DATA_CENTER", length = 256)
    protected String secondaryDataCenter;

    @Size(max = 256)
    @Column(name = "MI_URL_FOR_MORE_INFORMATION", length = 256)
    protected String urlForMoreInformation;

    @Size(max = 256)
    @Column(name = "MI_HARD_COPY_ON_FILE", length = 256)
    protected String hardCopyOnFile;

    @Size(max = 256)
    @Column(name = "MI_SITE_MAP", length = 256)
    protected String siteMap;

    @Size(max = 256)
    @Column(name = "MI_SITE_DIAGRAM", length = 256)
    protected String siteDiagram;

    @Size(max = 256)
    @Column(name = "MI_HORIZONTAL_MASK", length = 256)
    protected String horizonMask;

    @Size(max = 256)
    @Column(name = "MI_MONUMENT_DESCRIPTION", length = 256)
    protected String monumentDescription;

    @Size(max = 256)
    @Column(name = "MI_SITE_PICTIRES", length = 256)
    protected String sitePictures;

    @Size(max = 4000)
    @Column(name = "MI_NOTES", length = 4000)
    protected String notes;

    @Size(max = 256)
    @Column(name = "MI_ANTENNA_GRAPHICS", length = 256)
    protected String antennaGraphicsWithDimensions;

    @Size(max = 256)
    @Column(name = "MI_TEXT_GRAPHICS_FROM_ANTENNA", length = 256)
    protected String insertTextGraphicFromAntenna;

    @Size(max = 256)
    @Column(name = "MI_DIGITAL_OBJECT_ID", length = 256)
    protected String doi;
	
    /**
     * Return primary data center.
     */
    public String getPrimaryDataCenter() {
        return primaryDataCenter;
    }

    /**
     * Set primary data center.
     */
    public void setPrimaryDataCenter(String value) {
        this.primaryDataCenter = value;
    }

    /**
     * Return secondary data center.
     */
    public String getSecondaryDataCenter() {
        return secondaryDataCenter;
    }

    /**
     * Set secondary data center.
     */
    public void setSecondaryDataCenter(String value) {
        this.secondaryDataCenter = value;
    }

    /**
     * Return URL for more information.
     */
    public String getUrlForMoreInformation() {
        return urlForMoreInformation;
    }

    /**
     * Set URL for more information.
     */
    public void setUrlForMoreInformation(String value) {
        this.urlForMoreInformation = value;
    }

    /**
     * Return hard copy on file.
     */
    public String getHardCopyOnFile() {
        return hardCopyOnFile;
    }

    /**
     * Set hard copy on file.
     */
    public void setHardCopyOnFile(String value) {
        this.hardCopyOnFile = value;
    }

    /**
     * Return site map.
     */
    public String getSiteMap() {
        return siteMap;
    }

    /**
     * Set site map.
     */
    public void setSiteMap(String value) {
        this.siteMap = value;
    }

    /**
     * Return site diagram.
     */
    public String getSiteDiagram() {
        return siteDiagram;
    }

    /**
     * Set site diagram.
     */
    public void setSiteDiagram(String value) {
        this.siteDiagram = value;
    }

    /**
     * Return horizon mask.
     */
    public String getHorizonMask() {
        return horizonMask;
    }

    /**
     * Set horizon mask.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setHorizonMask(String value) {
        this.horizonMask = value;
    }

    /**
     * Return monument description.
     */
    public String getMonumentDescription() {
        return monumentDescription;
    }

    /**
     * Set monument description.
     */
    public void setMonumentDescription(String value) {
        this.monumentDescription = value;
    }

    /**
     * Return site pictures.
     */
    public String getSitePictures() {
        return sitePictures;
    }

    /**
     * Set site pictures.
     */
    public void setSitePictures(String value) {
        this.sitePictures = value;
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

    /**
     * Return antenna graphics with dimenstions.
     */
    public String getAntennaGraphicsWithDimensions() {
        return antennaGraphicsWithDimensions;
    }

    /**
     * Set antenna graphics with dimenstions.
     */
    public void setAntennaGraphicsWithDimensions(String value) {
        this.antennaGraphicsWithDimensions = value;
    }

    /**
     * Return insert text graphics from antenna.
     */
    public String getInsertTextGraphicFromAntenna() {
        return insertTextGraphicFromAntenna;
    }

    /**
     * Set insert text graphics from antenna.
     */
    public void setInsertTextGraphicFromAntenna(String value) {
        this.insertTextGraphicFromAntenna = value;
    }
	
    /**
     * Return insert digital object ID.
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Set insert digital object ID.
     */
    public void setDoi(String value) {
        this.doi = value;
    }
}
