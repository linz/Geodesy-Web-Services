package au.gov.ga.geodesy.domain.model;

public interface EventPublisher {

    void publish(Iterable<? extends Event> es);
}
