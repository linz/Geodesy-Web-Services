package au.gov.ga.geodesy.igssitelog.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import au.gov.ga.geodesy.domain.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("select e from Event e where e.timeHandled is null")
    List<Event> getPendingEvents();
}
