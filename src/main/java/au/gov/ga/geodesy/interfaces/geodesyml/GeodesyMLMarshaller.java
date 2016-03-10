package au.gov.ga.geodesy.interfaces.geodesyml;

import au.gov.xml.icsm.geodesyml.v_0_2_2.GeodesyMLType;

import java.io.Writer;

import javax.xml.bind.JAXBElement;

import java.io.Reader;

public interface GeodesyMLMarshaller {
    void marshal(GeodesyMLType doc, Writer writer) throws MarshallingException;
    JAXBElement<GeodesyMLType> unmarshal(Reader reader) throws MarshallingException;
}
