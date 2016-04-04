package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.SiteLogReaderFactory;

@Component
public class SiteLogSopacReaderFactory implements SiteLogReaderFactory {

    private static final Logger log = LoggerFactory.getLogger(SiteLogSopacReaderFactory.class);

    @Override
    public boolean canRecogniseInput(Reader input) {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(input));
            String rootElementName = doc.getDocumentElement().getNodeName();
            return "igsSiteLog".equals(rootElementName);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.debug("Error while trying to recognise root element", e);
            return false;
        }
    }

    @Override
    public SiteLogReader create(Reader input) {
        return new SiteLogSopacReader(input);
    }
}
