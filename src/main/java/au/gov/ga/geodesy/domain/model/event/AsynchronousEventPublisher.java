package au.gov.ga.geodesy.domain.model.event;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;

public class AsynchronousEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AsynchronousEventPublisher.class);

    @Autowired
    private EventRepository events;

    private List<EventSubscriber<?>> subscribers = new ArrayList<EventSubscriber<?>>();

    @PostConstruct
    public void startEventLoop() {
        new EventLoop(events).start();
    }

    public void subscribe(EventSubscriber<?> s) {
        subscribers.add(s);
    }

    public void publish(Event... es) {
        for (Event e : es) {
            for (EventSubscriber<?> s : subscribers) {
                try {
                    if (s.canHandle(e)) {
                        Event published = (Event) e.clone();
                        published.setSubscriber(s.getClass().toString());
                        events.save(published);
                    }
                } catch (CloneNotSupportedException ex) {
                    throw new GeodesyRuntimeException(ex);
                }
            }
            log.info("Scheduling event " + e + " for asynchronous publishing");
        }
    }

    public class EventLoop extends Thread {

        private EventRepository events;

        public EventLoop(EventRepository es) {
            events = es;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    Thread.sleep(3000);
                    List<Event> es = events.getPendingEvents();
                    log.info("Processing " + es.size() + " pending event(s)");
                    for (Event e : events.getPendingEvents()) {
                        e.published();
                        events.saveAndFlush(e);
                        synchronized(subscribers) {
                            for (EventSubscriber<?> s : subscribers) {
                                if (s.getClass().toString().equals(e.getSubscriber())) {
                                    handle(s, e);
                                }
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @SuppressWarnings("unchecked")
        private void handle(final EventSubscriber<?> s, final Event e) {
            log.info("publishing event " + e + " to " + s);
            try {
                ((EventSubscriber<Event>) s).handle(e);
            } catch (Exception ex) {
                e.setError(ExceptionUtils.getStackTrace(ex));
                ex.printStackTrace();
            }
            // TODO: try this again at some stage, it deadlocks
            /* new Thread() { */
            /*     @SuppressWarnings("unchecked") */
            /*     public void run() { */
            /*         log.info("publishing event " + e + " to " + s); */
            /*         try { */
            /*             ((EventSubscriber<Event>) s).handle(e); */
            /*         } catch (Exception e) { */
            /*             e.printStackTrace(); */
            /*             throw e; */
            /*         } */
            /*     } */
            /* }.start(); */
        }
    }

    public void handled(Event e) {
        e.handled();
        events.saveAndFlush(e);
    }
}
