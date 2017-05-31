package au.gov.ga.geodesy.domain.model;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.NewSiteRequestReceived;

@Transactional
public class NewSiteRequestRepositoryImpl extends SimpleJpaRepository<NewSiteRequest, Integer> implements NewSiteRequestRepositoryCustom {

    @Autowired
    private EventPublisher eventPublisher;

    public NewSiteRequestRepositoryImpl(EntityManager em) {
        super(NewSiteRequest.class, em);
    }

    @Override
    public <S extends NewSiteRequest> S save(S newSiteRequest) {
        S savedNewSiteRequest = super.save(newSiteRequest);
        eventPublisher.publish(new NewSiteRequestReceived(savedNewSiteRequest.getId()));
        return savedNewSiteRequest;
    }
}
