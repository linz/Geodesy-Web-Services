package au.gov.ga.geodesy.domain.service;

import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;

@Component
public class SiteLogReceivedNotificationService extends EventNotificationService<SiteLogReceived> {

    @Override
    public boolean canHandle(Event e) {
        return e instanceof SiteLogReceived;
    }
}
