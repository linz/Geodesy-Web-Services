package au.gov.ga.geodesy.domain.model;

public interface EventPublisher {
    void subscribe(EventSubscriber<?> s);
    void publish(Iterable<Event> es);
}
