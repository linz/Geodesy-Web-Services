package au.gov.ga.geodesy.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.EventSubscriber;
import au.gov.ga.geodesy.port.NotificationPort;

@Component
public class NotificationService implements EventSubscriber<Event> {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private List<NotificationPort> notificationPorts;
    
//    It would be good to have some kind of template here for the
//    responses.  So it can be injected and tested for.

    @Override
    public boolean canHandle(Event e) {
        return true;
    }

    @Override
    public void handle(Event e) {
        logger.debug("Handle event: " + e);

        for (NotificationPort notificationPort : notificationPorts) {
            notificationPort.sendNotification(e);
        }
    }
}
