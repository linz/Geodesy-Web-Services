package au.gov.ga.geodesy.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.gov.ga.geodesy.igssitelog.domain.model.EventRepository;

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

    public void publish(Iterable<Event> es) {
        for (Event e : es) {
            for (EventSubscriber<?> s : subscribers) {
                try {
                    Event published = (Event) e.clone();
                    published.setSubscriber(s.getClass().toString());
                    events.save(published);
                } catch (CloneNotSupportedException ex) {
                    throw new RuntimeException(ex);
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
                    System.out.println(subscribers.size());
                    for (Event e : events.getPendingEvents()) {
                        synchronized(subscribers) {
                            for (EventSubscriber<?> s : subscribers) {
                                if (s.canHandle(e) && s.getClass().toString().equals(e.getSubscriber())) {
                                    handle(s, e);
                                }
                            }
                        }
                    }
                }
            } catch (InterruptedException ok) {
            }
        }

        private void handle(final EventSubscriber<?> s, final Event e) {
            new Thread() {
                @SuppressWarnings("unchecked")
                public void run() {
                    ((EventSubscriber<Event>) s).handle(e);
                    log.info("Publishing event " + e + " to " + s);
                }
            }.start();
        }
    }
}
