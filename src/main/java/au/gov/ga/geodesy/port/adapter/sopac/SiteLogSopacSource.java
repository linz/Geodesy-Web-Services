package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.Reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.ga.geodesy.port.SiteLogSource;

@Configurable(preConstruction = true)
public class SiteLogSopacSource implements SiteLogSource {

    private SiteLog siteLog;

    @Autowired
    private IgsSiteLogXmlMarshaller marshaller;

    @Autowired
    private SiteLogSopacTranslator dtoTranslator;

    public SiteLogSopacSource(Reader sopacXML) throws MarshallingException {
        siteLog = dtoTranslator.fromDTO(marshaller.unmarshal(sopacXML));
    }

    public SiteLog getSiteLog() {
        return siteLog;
    }
}
