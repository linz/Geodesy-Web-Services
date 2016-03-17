package au.gov.ga.geodesy.port.adapter.sopac;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;

public interface SiteLogSopacTranslator {
    au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog fromDTO(IgsSiteLog siteLogSopac);
    IgsSiteLog toDTO(au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog siteLog);
}
