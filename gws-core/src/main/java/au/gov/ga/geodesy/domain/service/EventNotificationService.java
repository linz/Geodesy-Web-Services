package au.gov.ga.geodesy.domain.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventSubscriber;
import au.gov.ga.geodesy.port.Notification;
import au.gov.ga.geodesy.port.NotificationPort;

@Transactional("geodesyTransactionManager")
public abstract class EventNotificationService<E extends Event>  implements EventSubscriber<E> {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationService.class);

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private NotificationPort notification;

    protected ObjectMapper json = new ObjectMapper();

    public EventNotificationService() {
        json = new ObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    private void subcribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event e) {
        return true;
    }

    protected Notification toNotification(E event) {
        return new Notification() {

            public String getSubject() {
                return event.getName();
            }

            public String getBody() {
                try {
                    StringBuilder body = new StringBuilder();
                    body.append("We have received a new event.\n\n");
                    body.append(json.writeValueAsString(event));
                    return body.toString();
                } catch (JsonProcessingException e) {
                    String errorMessage = "Failed to serialise event with id " + event.getId();
                    log.error(errorMessage, e);
                    return errorMessage;
                }
            }
        };
    }

    public void handle(E event) {
        notification.sendNotification(toNotification(event));
        eventPublisher.handled(event);
        log.info("Handled event " + event);
    }
}
