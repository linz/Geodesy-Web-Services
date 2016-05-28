package au.gov.ga.geodesy.port.adapter.smtp;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.InvalidSiteLogReceived;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.domain.model.event.SiteUpdated;
import au.gov.ga.geodesy.domain.model.event.WeeklySolutionAvailable;
import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.port.NotificationPort;
import au.gov.ga.geodesy.support.email.GeodesyMailMessage;
import au.gov.ga.geodesy.support.properties.GeodesyNotificationsConfig;

@Component
public class SMTPNotificationAdapter implements NotificationPort {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SMTPAdapter smtpAdapter;

    @Autowired
    private GeodesyNotificationsConfig mailNotifications;

    @Override
    public void sendNotification(Event e) {
        String[] to = getNotificationTo(e);
        String from = mailNotifications.getFromEmail();
        String subject = buildEventSubject(e);

        GeodesyMailMessage message = new GeodesyMailMessage(from, subject, to);
        message.setBody(e.getMessage());

        logger.debug("sendNotification [" + message + "]");
        smtpAdapter.sendEmail(message);
    }

    private String buildEventSubject(Event e) {
        return "Geodesy notification - " + camelSplit(e.getClass().getSimpleName());
    }

    private String camelSplit(String className) {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(className));
    }

    private String[] getNotificationTo(Event e) {
        // TODO: could we avoid using instanceof here?
        if (e instanceof InvalidSiteLogReceived) {
            return mailNotifications.getInvalidSiteLogReceivedEmailAddressees().toArray(new String[0]);
        } else if (e instanceof SiteLogReceived) {
            return mailNotifications.getSiteLogReceivedEmailAddressees().toArray(new String[0]);
        } else if (e instanceof SiteUpdated) {
            return mailNotifications.getSiteUpdatedEmailAddressees().toArray(new String[0]);
        } else if (e instanceof WeeklySolutionAvailable) {
            return mailNotifications.getWeeklySolutionAvailableEmailAddressees().toArray(new String[0]);
        } else {
            throw new GeodesyRuntimeException("Unknown Event class: " + e.getClass());
        }
    }
}
