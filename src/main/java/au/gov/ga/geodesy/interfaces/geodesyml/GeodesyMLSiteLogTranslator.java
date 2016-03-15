package au.gov.ga.geodesy.interfaces.geodesyml;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.xml.icsm.geodesyml.v_0_2_2.GeodesyMLType;

public interface GeodesyMLSiteLogTranslator {
    public GeodesyMLType dozerTranslate(IgsSiteLog sopacSiteLog);
}
