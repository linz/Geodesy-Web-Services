package au.gov.ga.geodesy.domain.model.event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_REGISTRATION_RECEIVED")
@DiscriminatorValue("user registration received")
public class UserRegistrationReceived extends Event {

    @Column(name = "USER_REGISTRATION_ID", nullable = false)
    private Integer userRegistrationId;

    public UserRegistrationReceived(Integer userRegistrationId) {
        this.userRegistrationId = userRegistrationId;
    }

    public Integer getUserRegistrationId() {
        return userRegistrationId;
    }
}
