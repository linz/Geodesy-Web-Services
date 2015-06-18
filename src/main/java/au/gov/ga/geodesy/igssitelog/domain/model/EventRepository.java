package au.gov.ga.geodesy.igssitelog.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import au.gov.ga.geodesy.domain.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
