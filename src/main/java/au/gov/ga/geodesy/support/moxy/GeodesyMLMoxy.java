package au.gov.ga.geodesy.support.moxy;

import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextFactory;

import au.gov.ga.geodesy.interfaces.xml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.interfaces.xml.MarshallingException;
import au.gov.xml.icsm.geodesyml.v_0_2_1.GeodesyMLType;

public class GeodesyMLMoxy implements GeodesyMLMarshaller {

    private JAXBContext jaxbContext;

    public GeodesyMLMoxy() throws MarshallingException {
        try {
            jaxbContext = JAXBContextFactory.createContext(new Class[] {GeodesyMLType.class}, new Properties());
        } catch (JAXBException e) {
            throw new MarshallingException("Failed to initialise JAXBContext", e);
        }
    }

    private Marshaller createMarshaller() throws MarshallingException {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            return marshaller;
        } catch (JAXBException e) {
            throw new MarshallingException("Failed to create marshaller", e);
        }
    }

    private Unmarshaller createUnmarshaller() throws MarshallingException {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller;
        } catch (JAXBException e) {
            throw new MarshallingException("Failed to create unmarshaller", e);
        }
    }

    public void marshal(GeodesyMLType site, Writer writer) throws MarshallingException {
        try {
            createMarshaller().marshal(site, writer);
        } catch (JAXBException e) {
            throw new MarshallingException("Failed to marshal a site log", e);
        }
    }

    @SuppressWarnings("unchecked")
    public JAXBElement<GeodesyMLType> unmarshal(Reader reader) throws MarshallingException {
        try {
            return (JAXBElement<GeodesyMLType>) createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new MarshallingException("Failed to unmarshal a site log", e);
        }
    }
}
