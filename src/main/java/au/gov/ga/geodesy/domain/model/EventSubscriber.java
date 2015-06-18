package au.gov.ga.geodesy.domain.model;

public interface EventSubscriber<T extends Event> {
    boolean canHandle(Event e);
    void handle(T e);
}
