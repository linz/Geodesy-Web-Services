package au.gov.ga.geodesy.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import au.gov.ga.geodesy.domain.model.UserRegistration;
import au.gov.ga.geodesy.domain.model.UserRegistrationRepository;
import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.UserRegistrationReceived;
import au.gov.ga.geodesy.port.Notification;

@Component
@Transactional("geodesyTransactionManager")
public class UserRegistrationNotificationService extends EventNotificationService<UserRegistrationReceived> {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationNotificationService.class);

    @Autowired
    private UserRegistrationRepository userRegistrations;

    @Override
    public boolean canHandle(Event e) {
        return e instanceof UserRegistrationReceived;
    }

    @Override
    public Notification toNotification(UserRegistrationReceived event) {
        Integer id = event.getUserRegistrationId();
        final UserRegistration registration = userRegistrations.findOne(id);

        return new Notification() {

            public String getSubject() {
                return event.getName();
            }

            public String getBody() {
                try {
                    StringBuilder body = new StringBuilder();
                    body.append("We have received a new user registration request.\n\n");
                    body.append(UserRegistrationNotificationService.super.json.writeValueAsString(registration));
                    return body.toString();
                } catch (JsonProcessingException e) {
                    String errorMessage = "Failed to serialise user registration object with id " + id;
                    log.error(errorMessage, e);
                    return errorMessage;
                }
            }
        };
    }
}
