package au.gov.ga.geodesy.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SITE_LOG_UPLOADED")
@DiscriminatorValue("site log uploaded")
public class SiteLogUploaded extends Event {

    @Column(name = "FOUR_CHARACTER_ID", nullable = false)
    private String fourCharacterId;

    @SuppressWarnings("unused") // used by hibernate
    private SiteLogUploaded() {
    }

    public SiteLogUploaded(String fourCharacterId) {
        this.fourCharacterId = fourCharacterId;
    }

    public String getFourCharacterId() {
        return fourCharacterId;
    }
}
