package au.gov.ga.geodesy.domain.model.event;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import au.gov.ga.geodesy.support.spring.AggregateRepository;

public interface EventRepository extends AggregateRepository<Event>, EventRepositoryCustom {
    @Override
    List<Event> findAll();

    @Query("select e from Event e where e.retries = " + Event.MAX_RETRIES)
    List<Event> findFailed();
}
