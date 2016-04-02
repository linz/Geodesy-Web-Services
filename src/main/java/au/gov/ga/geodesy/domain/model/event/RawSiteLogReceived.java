package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RAW_SITE_LOG_RECEIVED")
@DiscriminatorValue("raw site log received")
public class RawSiteLogReceived extends Event {

    @Column(name = "SITE_LOG_TEXT", length=1048576, nullable = false)
    private String siteLogText;

    @SuppressWarnings("unused") // used by hibernate
    private RawSiteLogReceived() {
    }

    public RawSiteLogReceived(String siteLogText) {
        this.siteLogText = siteLogText;
    }

    public String getSiteLogText() {
        return siteLogText;
    }
}
