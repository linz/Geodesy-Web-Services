package au.gov.ga.geodesy.domain.model.event;

public interface EventPublisher {
    void subscribe(EventSubscriber<?> s);
    void publish(Event... es);
    void handled(Event e);
}
