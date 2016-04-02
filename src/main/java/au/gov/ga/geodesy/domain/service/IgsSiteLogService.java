package au.gov.ga.geodesy.domain.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventSubscriber;
import au.gov.ga.geodesy.domain.model.event.InvalidSiteLogReceived;
import au.gov.ga.geodesy.domain.model.event.RawSiteLogReceived;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.SiteLogReaderAbstractFactory;

@Component
@Transactional("geodesyTransactionManager")
public class IgsSiteLogService implements EventSubscriber<RawSiteLogReceived> {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogService.class);

    @Autowired
    private SiteLogRepository siteLogs;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private SiteLogReaderAbstractFactory siteLogReaderFactory;

    public void upload(SiteLog siteLog) {
        siteLogs.delete(siteLog.getSiteIdentification().getFourCharacterId());
        siteLogs.save(siteLog);
        String id = siteLog.getSiteIdentification().getFourCharacterId();
        log.info("Saving site log: " + id);
        eventPublisher.publish(new SiteLogReceived(id));
    }

    public void upload(Reader reader) {
        try (Scanner scanner = new Scanner(reader)) {
            scanner.useDelimiter("\\Z");
            String siteLogText = scanner.next();
            eventPublisher.publish(new RawSiteLogReceived(siteLogText));
        }
    }

    @PostConstruct
    private void subscribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event e) {
        return e instanceof RawSiteLogReceived;
    }

    public void handle(RawSiteLogReceived rawSiteLogReceived) {
        log.info("Received rawSiteLogReceived event");
        String siteLogText = rawSiteLogReceived.getSiteLogText();
        try {
            Optional<SiteLogReader> siteLogReader = siteLogReaderFactory.create(new StringReader(siteLogText));
            if (siteLogReader.isPresent()) {
                upload(siteLogReader.get().getSiteLog());
            } else {
                // TODO: Include a message saying shy the site log is invalid
                eventPublisher.publish(new InvalidSiteLogReceived(siteLogText));
            }
        }
        catch (InvalidSiteLogException e) {
            // TODO: Include a message saying shy the site log is invalid
            eventPublisher.publish(new InvalidSiteLogReceived(siteLogText));
        }
        eventPublisher.handled(rawSiteLogReceived);
    }
}
