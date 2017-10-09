package au.gov.ga.geodesy.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.port.Notification;

@Component
public class SiteLogReceivedNotificationService extends EventNotificationService<SiteLogReceived> {

    private static final Logger log = LoggerFactory.getLogger(SiteLogReceivedNotificationService.class);

    private ObjectMapper filteredJson;

    @Override
    public boolean canHandle(Event e) {
        return e instanceof SiteLogReceived;
    }

    @JsonFilter("excludeSiteLogText")
    public static class SiteLogReceivedFiltered {}

    public SiteLogReceivedNotificationService() {
        super();
        this.filteredJson = json.copy();

        FilterProvider filterProvider = new SimpleFilterProvider()
            .addFilter("excludeSiteLogText",
                       SimpleBeanPropertyFilter.serializeAllExcept("siteLogText"));

        filteredJson.addMixIn(SiteLogReceived.class, SiteLogReceivedFiltered.class);
        filteredJson.setFilterProvider(filterProvider);
    }

    @Override
    protected Notification toNotification(SiteLogReceived event) {
        return new Notification() {

            public String getSubject() {
                return event.getName();
            }

            public String getBody() {
                try {
                    StringBuilder body = new StringBuilder();
                    body.append(filteredJson.writeValueAsString(event));
                    return body.toString();
                } catch (JsonProcessingException e) {
                    String errorMessage = "Failed to serialise event with id " + event.getId();
                    log.error(errorMessage, e);
                    return errorMessage;
                }
            }
        };
    }
}
