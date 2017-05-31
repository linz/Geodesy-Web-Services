package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "NEW_SITE_REQUEST_RECEIVED")
@DiscriminatorValue("new site requested")
public class NewSiteRequestReceived extends Event {

    @Column(name = "NEW_SITE_REQUEST_ID", nullable = false)
    private Integer newSiteRequestId;


    @SuppressWarnings("unused") // used by hibernate
    private NewSiteRequestReceived() {
    }

    public NewSiteRequestReceived(Integer newSiteRequestId) {
        this.newSiteRequestId = newSiteRequestId;
    }

    public Integer getNewSiteRequestId() {
        return newSiteRequestId;
    }
}
