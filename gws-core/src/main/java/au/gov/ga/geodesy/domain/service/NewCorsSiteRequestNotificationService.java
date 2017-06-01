package au.gov.ga.geodesy.domain.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import au.gov.ga.geodesy.domain.model.NewCorsSiteRequest;
import au.gov.ga.geodesy.domain.model.NewCorsSiteRequestRepository;
import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventSubscriber;
import au.gov.ga.geodesy.domain.model.event.NewCorsSiteRequestReceived;
import au.gov.ga.geodesy.port.Notification;
import au.gov.ga.geodesy.port.NotificationPort;


/**
 * This service subscribes to the New Site Request event and sends a notification when one occurs.
 */
@Component
@Transactional("geodesyTransactionManager")
public class NewCorsSiteRequestNotificationService implements EventSubscriber<NewCorsSiteRequestReceived> {

    private static final Logger log = LoggerFactory.getLogger(NewCorsSiteRequestNotificationService.class);

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private NewCorsSiteRequestRepository newCorsSiteRequestsRepository;

    @Autowired
    private NotificationPort notification;

    private ObjectMapper json = new ObjectMapper();

    public NewCorsSiteRequestNotificationService() {
        json = new ObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    private void subcribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event e) {
        return e != null && (e instanceof NewCorsSiteRequestReceived);
    }

    public void handle(NewCorsSiteRequestReceived event) {
        Integer id = event.getNewCorsSiteRequestId();
        NewCorsSiteRequest newCorsSiteRequest = newCorsSiteRequestsRepository.findOne(id);
        notification.sendNotification(new Notification() {
            public String getSubject() {
                return event.getName();
            }

            public String getBody() {
                try {
                    StringBuilder body = new StringBuilder();
                    body.append("We have received a new site creation request.\n\n");
                    body.append(json.writeValueAsString(newCorsSiteRequest));
                    return body.toString();
                } catch (JsonProcessingException e) {
                    String errorMessage = "Failed to serialise new site request object with id " + id;
                    log.error(errorMessage, e);
                    return errorMessage;
                }
            }
        });
        eventPublisher.handled(event);
        log.info("Handled event " + event);
    }
}
