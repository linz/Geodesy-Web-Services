package au.gov.ga.geodesy.domain.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.igssitelog.domain.model.EventRepository;

@Component
public class EventRepositoryPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventRepositoryPublisher.class);

    @Autowired
    private EventRepository events;

    public void publish(Iterable<? extends Event> es) {
        for (Event e : es) {
            events.save(e);
            log.info("Publishing event: " + e);
        }
    }
}
