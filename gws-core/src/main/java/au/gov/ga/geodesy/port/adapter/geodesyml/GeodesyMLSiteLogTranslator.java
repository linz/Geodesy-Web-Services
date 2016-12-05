package au.gov.ga.geodesy.port.adapter.geodesyml;

import javax.xml.bind.JAXBElement;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.xml.icsm.geodesyml.v_0_3.GeodesyMLType;

public interface GeodesyMLSiteLogTranslator {
    public JAXBElement<GeodesyMLType> dozerTranslate(IgsSiteLog sopacSiteLog);
}
