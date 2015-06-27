package au.gov.ga.geodesy.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.SiteLogUploaded;
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

    public void upload(IgsSiteLog siteLog) throws Exception {
        siteLogs.delete(siteLog.getSiteIdentification().getFourCharacterId());
        siteLogs.save(siteLog);
        String id = siteLog.getSiteIdentification().getFourCharacterId();
        log.info("Saving " + id);
        eventPublisher.publish(new SiteLogUploaded(id));
    }
}
