package au.gov.ga.geodesy.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import au.gov.ga.geodesy.domain.model.Event;
import au.gov.ga.geodesy.domain.model.SiteLogUploaded;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;

@Component
public class IgsSiteLogService {

    private static final Logger log = LoggerFactory.getLogger(IgsSiteLogService.class);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    public List<Event> upload(IgsSiteLog siteLog) throws Exception {
        siteLogs.delete(siteLog.getSiteIdentification().getFourCharacterId());
        siteLogs.save(siteLog);
        String id = siteLog.getSiteIdentification().getFourCharacterId();
        log.info("Saving " + id);
        return Lists.newArrayList((Event) new SiteLogUploaded(id));
    }
}
