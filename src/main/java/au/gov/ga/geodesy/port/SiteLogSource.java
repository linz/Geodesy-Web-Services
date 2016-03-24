package au.gov.ga.geodesy.port;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;

public interface SiteLogSource {
    SiteLog getSiteLog() throws InvalidSiteLogException;
}
