package au.gov.ga.geodesy.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;

@Component
public class IgsSiteLogService {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogService.class);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    public void upload(IgsSiteLog siteLog) throws Exception {
        siteLogs.save(siteLog);
        log.info("Saving " + siteLog.getSiteIdentification().getFourCharacterId());
    }
}
