package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.io.Reader;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.support.mapper.orika.geodesyml.SiteLogMapper;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import au.gov.xml.icsm.geodesyml.v_0_4.GeodesyMLType;
import au.gov.xml.icsm.geodesyml.v_0_4.SiteLogType;

/**
 * GeodesyML site log reader
 */
@Configurable
public class GeodesyMLSiteLogReader extends SiteLogReader {

    private GeodesyMLMarshaller marshaller = new GeodesyMLMoxy();

    @Autowired
    private SiteLogMapper siteLogMapper;

    /**
     * {@inheritDoc}
     */
    public GeodesyMLSiteLogReader(Reader reader) {
        super(reader);
    }

    /**
     * {@inheritDoc}
     */
    public SiteLog getSiteLog(String siteLogText) throws InvalidSiteLogException {
        try {
            GeodesyMLType geodesyML = marshaller.unmarshal(new StringReader(siteLogText), GeodesyMLType.class).getValue();
            return GeodesyMLUtils
                    .getElementFromJAXBElements(geodesyML.getElements(), SiteLogType.class)
                    .map(siteLogMapper::to)
                    .findFirst()
                    .get(); // TODO: are there any
        }
        catch (MarshallingException e) {
            throw new InvalidSiteLogException(e);
        }
    }
}
