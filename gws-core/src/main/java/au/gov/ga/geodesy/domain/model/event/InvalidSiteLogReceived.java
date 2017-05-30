package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "INVALID_SITE_LOG_RECEIVED")
@DiscriminatorValue("invalid site log received")
public class InvalidSiteLogReceived extends Event {

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
}
