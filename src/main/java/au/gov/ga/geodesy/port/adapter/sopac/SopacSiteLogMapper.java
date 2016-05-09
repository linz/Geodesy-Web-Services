package au.gov.ga.geodesy.port.adapter.sopac;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;

public interface SopacSiteLogMapper {
    SiteLog fromDTO(IgsSiteLog siteLogSopac);
    IgsSiteLog toDTO(SiteLog siteLog);
}
