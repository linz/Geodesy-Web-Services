package au.gov.ga.geodesy.port.adapter.mock;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.port.Notification;
import au.gov.ga.geodesy.port.NotificationPort;
import au.gov.ga.geodesy.port.adapter.sns.SnsNotificationAdapter;

public class InMemoryNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(SnsNotificationAdapter.class);

    private List<Notification> notifications = new ArrayList<>();

    @Override
    public void sendNotification(Notification notification) {
        log.info("Published event " + notification.getSubject() + " to memory; retreive using getNotifications().");
        notifications.add(notification);
    }

    @Override
    public void sendNotification(Event event) {
        sendNotification(notification(event));
    }

    private Notification notification(Event event) {
        return new Notification() {
            public String getSubject() {
                return event.getName();
            }

            public String getBody() {
                return event.toString();
            }
        };
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
