package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.Reader;
import java.io.StringReader;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.ga.geodesy.port.InvalidSiteLogException;
import au.gov.ga.geodesy.port.SiteLogReader;

@Configurable(preConstruction = true)
public class SopacSiteLogReader extends SiteLogReader {

    private @MonotonicNonNull SiteLog siteLog;

    @Autowired
    @SuppressWarnings("initialization.fields.uninitialized")
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    @SuppressWarnings("initialization.fields.uninitialized")
    private SopacSiteLogMapper mapper;

    /**
     * {@inheritDoc}
     */
    public SopacSiteLogReader(Reader input) {
        super(input);
    }

    /**
     * {@inheritDoc}
     */
    public SiteLog getSiteLog(String siteLogText) throws InvalidSiteLogException {
        try {
            if (siteLog == null) {
                siteLog = mapper.fromDTO(marshaller.unmarshal(new StringReader(siteLogText)));
            }
            return siteLog;
        }
        catch (MarshallingException e) {
            throw new InvalidSiteLogException(e);
        }
    }
}
