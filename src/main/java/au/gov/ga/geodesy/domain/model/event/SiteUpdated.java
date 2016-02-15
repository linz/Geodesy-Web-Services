package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SITE_UPDATED")
@DiscriminatorValue("site updated")
public class SiteUpdated extends Event {

    @Column(name = "FOUR_CHARACTER_ID", nullable = false)
    private String fourCharacterId;

    @SuppressWarnings("unused") // used by hibernate
    private SiteUpdated() {
    }

    public SiteUpdated(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }
}
