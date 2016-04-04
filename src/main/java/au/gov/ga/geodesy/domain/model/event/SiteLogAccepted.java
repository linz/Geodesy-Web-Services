package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SITE_LOG_ACCEPTED")
@DiscriminatorValue("site log accepted")
public class SiteLogAccepted extends Event {

    @Column(name = "FOUR_CHARACTER_ID", nullable = false)
    private String fourCharacterId;

    @SuppressWarnings("unused") // used by hibernate
    private SiteLogAccepted() {
    }

    public SiteLogAccepted(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }
}
