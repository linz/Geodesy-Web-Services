package au.gov.ga.geodesy.domain.model.sitelog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Past;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/monumentInfo/2004/formInformation.xsd:formInformationType
 */
@Embeddable
public class FormInformation {

    @Column(name = "FORM_PREPARED_BY", length = 200)
    protected String preparedBy;

    @Column(name = "FORM_DATE_PREPARED")
    @Past
    protected Date datePrepared;

    @Column(name = "FORM_REPORT_TYPE", length = 200)
    protected String reportType;

    /**
     * Return prepered by.
     */
    public String getPreparedBy() {
        return preparedBy;
    }

    /**
     * Set prepared by.
     */
    public void setPreparedBy(String value) {
        this.preparedBy = value;
    }

    /**
     * Return date prepared.
     */
    public Date getDatePrepared() {
        return datePrepared;
    }

    /**
     * Set date prepared.
     */
    public void setDatePrepared(Date value) {
        this.datePrepared = value;
    }

    /**
     * Return report type.
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * Set report type.
     */
    public void setReportType(String value) {
        this.reportType = value;
    }
}
