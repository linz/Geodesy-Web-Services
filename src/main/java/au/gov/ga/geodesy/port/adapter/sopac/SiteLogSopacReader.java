package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.Reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.ga.geodesy.port.SiteLogReader;

@Configurable(preConstruction = true)
public class SiteLogSopacReader extends SiteLogReader {

    private SiteLog siteLog;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    private SiteLogSopacMapper mapper;

    public SiteLogSopacReader(Reader sopacXML) throws MarshallingException {
        super(sopacXML);
        siteLog = mapper.fromDTO(marshaller.unmarshal(sopacXML));
    }

    public SiteLog getSiteLog() {
        return siteLog;
    }
}
