package au.gov.ga.geodesy.domain.model.event;

import java.util.List;

public interface EventRepositoryCustom {
    List<Event> getPendingEvents();
}

