package au.gov.ga.geodesy.port;

import au.gov.ga.geodesy.domain.model.event.Event;

public interface NotificationPort {
    public void sendNotification(Event e);
    public void sendNotification(Notification n);
}
