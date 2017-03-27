package au.gov.ga.geodesy.domain.model;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.UserRegistrationReceived;

public class UserRegistrationRepositoryImpl extends SimpleJpaRepository<UserRegistration, Integer> implements UserRegistrationRepositoryCustom {

    @Autowired
    private EventPublisher eventPublisher;

    public UserRegistrationRepositoryImpl(EntityManager em) {
        super(UserRegistration.class, em);
    }

    public <S extends UserRegistration> S save(S userRegistration) {
        S savedRegistration = super.save(userRegistration);
        eventPublisher.publish(new UserRegistrationReceived(savedRegistration.getId()));
        return savedRegistration;
    }
}
