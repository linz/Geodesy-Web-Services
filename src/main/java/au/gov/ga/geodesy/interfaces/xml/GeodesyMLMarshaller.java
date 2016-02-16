package au.gov.ga.geodesy.interfaces.xml;

import au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException;
import au.gov.xml.icsm.geodesyml.v_0_2_1.GeodesyMLType;

import java.io.Writer;

import javax.xml.bind.JAXBElement;

import java.io.Reader;

public interface GeodesyMLMarshaller {
    void marshal(GeodesyMLType doc, Writer writer) throws MarshallingException;
    JAXBElement<GeodesyMLType> unmarshal(Reader reader) throws MarshallingException;
}
