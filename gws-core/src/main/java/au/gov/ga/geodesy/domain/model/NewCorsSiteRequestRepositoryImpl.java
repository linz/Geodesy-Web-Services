package au.gov.ga.geodesy.domain.model;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.NewCorsSiteRequestReceived;

@Transactional
public class NewCorsSiteRequestRepositoryImpl extends SimpleJpaRepository<NewCorsSiteRequest, Integer>
	implements NewCorsSiteRequestRepositoryCustom {

    @Autowired
    private EventPublisher eventPublisher;

    public NewCorsSiteRequestRepositoryImpl(EntityManager em) {
        super(NewCorsSiteRequest.class, em);
    }

    @Override
    public <S extends NewCorsSiteRequest> S save(S newCorsSiteRequest) {
        S savedNewCorsSiteRequest = super.save(newCorsSiteRequest);
        eventPublisher.publish(new NewCorsSiteRequestReceived(savedNewCorsSiteRequest.getId()));
        return savedNewCorsSiteRequest;
    }
}
