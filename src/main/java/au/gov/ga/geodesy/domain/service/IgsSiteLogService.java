package au.gov.ga.geodesy.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;

@Component
@Transactional("geodesyTransactionManager")
public class IgsSiteLogService {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogService.class);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private EventPublisher eventPublisher;

    public void upload(IgsSiteLog siteLog) {
        siteLogs.delete(siteLog.getSiteIdentification().getFourCharacterId());
        siteLogs.save(siteLog);
        String id = siteLog.getSiteIdentification().getFourCharacterId();
        log.info("Saving site log: " + id);
        eventPublisher.publish(new SiteLogReceived(id));
    }
}
