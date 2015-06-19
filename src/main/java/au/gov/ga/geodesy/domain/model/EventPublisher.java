package au.gov.ga.geodesy.domain.model;

public interface EventPublisher {

    void subscribe(EventSubscriber<?> s);
    void unsubscribeAll();
    void publish(Iterable<Event> es);

}
