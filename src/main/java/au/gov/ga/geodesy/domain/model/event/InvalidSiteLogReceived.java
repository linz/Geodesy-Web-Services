package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "INVALID_SITE_LOG_RECEIVED")
@DiscriminatorValue("invalid site log received")
public class InvalidSiteLogReceived extends Event {
    private static final Integer extractAmount = 100;

    @Column(name = "SITE_LOG_TEXT", length = 1048576, nullable = false)
    private String siteLogText;

    @SuppressWarnings("unused") // used by hibernate
    private InvalidSiteLogReceived() {
    }

    public InvalidSiteLogReceived(String siteLogText) {
        this.siteLogText = siteLogText;
    }

    public String getSiteLogText() {
        return siteLogText;
    }

    @Transient
    /**
     * Return a Human digestable message about this event. Used in email for example.
     * 
     * @return the message
     */
    public String getMessage() {
        String message = super.getMessage() + ", First 100 characters of sitelog: " + getSiteLogTextExtract();
        return message;
    }

    private String getSiteLogTextExtract() {
        String text = getSiteLogText();
        if (!StringUtils.isBlank(text)) {
            return "\"" + text.substring(0, extractAmount < text.length() ? extractAmount : text.length()) + "\"";
        } else {
            return "No site log text";
        }
    }
}
