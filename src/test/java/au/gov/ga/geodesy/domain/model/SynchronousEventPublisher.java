package au.gov.ga.geodesy.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronousEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SynchronousEventPublisher.class);

    private ThreadLocal<List<EventSubscriber<?>>> subscribers = new ThreadLocal<List<EventSubscriber<?>>>();

    public SynchronousEventPublisher() {
        subscribers.set(new ArrayList<EventSubscriber<?>>());
    }

    public void subscribe(EventSubscriber<?> subscriber) {
        subscribers.get().add(subscriber);
    }

    public void publish(Event... es) {
        for (Event e : es) {
            publish(e);
            log.info("Publishing event: " + e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void publish(T e) {
        for (EventSubscriber<?> s : subscribers.get()) {
            if (s.canHandle(e)) {
                ((EventSubscriber<T>) s).handle(e);
                e.handled();
            }
        }
    }

    public void handled(Event e) {
        e.handled();
    }
}
