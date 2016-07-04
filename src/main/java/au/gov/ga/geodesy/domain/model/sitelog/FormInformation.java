package au.gov.ga.geodesy.domain.model.sitelog;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Past;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/monumentInfo/2004/formInformation.xsd:formInformationType
 */
@Embeddable
public class FormInformation {

    @Column(name = "FORM_PREPARED_BY", length = 200)
    protected @MonotonicNonNull String preparedBy;

    @Column(name = "FORM_DATE_PREPARED")
    @Past
    protected @MonotonicNonNull Instant datePrepared;

    @Column(name = "FORM_REPORT_TYPE", length = 200)
    protected @MonotonicNonNull String reportType;

    /**
     * Return prepered by.
     */
    public @Nullable String getPreparedBy() {
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
    public @Nullable Instant getDatePrepared() {
        return datePrepared;
    }

    /**
     * Set date prepared.
     */
    public void setDatePrepared(Instant value) {
        this.datePrepared = value;
    }

    /**
     * Return report type.
     */
    public @Nullable String getReportType() {
        return reportType;
    }

    /**
     * Set report type.
     */
    public void setReportType(String value) {
        this.reportType = value;
    }
}
