package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "SITE_LOG_RECEIVED")
@DiscriminatorValue("site log received")
@PrimaryKeyJoinColumn(foreignKey=@ForeignKey(name = "fk_domain_event_site_log_received"))
public class SiteLogReceived extends Event {

    @Column(name = "FOUR_CHAR_ID", nullable = false)
    private String fourCharacterId;

    @SuppressWarnings({"unused", "initialization.fields.uninitialized"}) // used by hibernate
    private SiteLogReceived() {
    }

    public SiteLogReceived(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }
}

