package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "NEW_CORS_SITE_REQUEST_RECEIVED")
@DiscriminatorValue("new CORS site requested")
public class NewCorsSiteRequestReceived extends Event {

    @Column(name = "NEW_CORS_SITE_REQUEST_ID", nullable = false)
    private Integer newCorsSiteRequestId;


    @SuppressWarnings("unused") // used by hibernate
    private NewCorsSiteRequestReceived() {
    }

    public NewCorsSiteRequestReceived(Integer newCorsSiteRequestId) {
        this.newCorsSiteRequestId = newCorsSiteRequestId;
    }

    public Integer getNewCorsSiteRequestId() {
        return newCorsSiteRequestId;
    }
}
