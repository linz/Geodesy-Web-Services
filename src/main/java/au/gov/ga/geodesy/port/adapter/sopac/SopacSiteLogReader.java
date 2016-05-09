package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.Reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;

@Configurable(preConstruction = true)
public class SopacSiteLogReader extends SiteLogReader {

    private SiteLog siteLog;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    private SopacSiteLogMapper mapper;

    public SopacSiteLogReader(Reader input) {
        super(input);
    }

    public SiteLog getSiteLog() throws InvalidSiteLogException {
        try {
            if (siteLog == null) {
                siteLog = mapper.fromDTO(marshaller.unmarshal(input));
            }
            return siteLog;
        }
        catch (MarshallingException e) {
            throw new InvalidSiteLogException(e);
        }
    }
}
