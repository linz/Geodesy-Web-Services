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

import au.gov.ga.geodesy.domain.model.UserRegistration;
import au.gov.ga.geodesy.domain.model.UserRegistrationRepository;
import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventSubscriber;
import au.gov.ga.geodesy.domain.model.event.UserRegistrationReceived;
import au.gov.ga.geodesy.port.Notification;
import au.gov.ga.geodesy.port.NotificationPort;

@Component
@Transactional("geodesyTransactionManager")
public class UserRegistrationNotificationService implements EventSubscriber<UserRegistrationReceived> {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationNotificationService.class);

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private UserRegistrationRepository userRegistrations;

    @Autowired
    private NotificationPort notification;

    private ObjectMapper json = new ObjectMapper();

    public UserRegistrationNotificationService() {
        json = new ObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    private void subcribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event e) {
        return e != null && (e instanceof UserRegistrationReceived);
    }

    public void handle(UserRegistrationReceived event) {
        Integer id = event.getUserRegistrationId();
        UserRegistration registration = userRegistrations.findOne(id);
        notification.sendNotification(new Notification() {
            public String getSubject() {
                return event.getName();
            }

            public String getBody() {
                try {
                    StringBuilder body = new StringBuilder();
                    body.append("We have received a new user registration request.\n\n");
                    body.append(json.writeValueAsString(registration));
                    return body.toString();
                } catch (JsonProcessingException e) {
                    String errorMessage = "Failed to serialise user registration object with id " + id;
                    log.error(errorMessage, e);
                    return errorMessage;
                }
            }
        });
        eventPublisher.handled(event);
        log.info("Handled event " + event);
    }
}
