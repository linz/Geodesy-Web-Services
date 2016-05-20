package au.gov.ga.geodesy.domain.model.event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Integer>, EventRepositoryCustom {
    @Override
    List<Event> findAll();

    @Query("select e from Event e where e.retries = " + Event.MAX_RETRIES)
    List<Event> findFailed();
}
