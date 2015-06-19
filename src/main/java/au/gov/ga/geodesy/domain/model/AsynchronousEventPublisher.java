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

    public void unsubscribeAll() {
        subscribers.clear();
    }

    public void publish(Iterable<Event> es) {
        for (Event e : es) {
            events.save(e);
            log.info("Publishing event: " + e);
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
                    System.out.println(events);
                }

            } catch (InterruptedException ok) {
            }
        }
    }
}
