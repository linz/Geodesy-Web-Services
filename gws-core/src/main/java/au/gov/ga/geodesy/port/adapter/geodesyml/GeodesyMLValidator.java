package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.xmlschemer.SchemaValidator;
import au.gov.ga.xmlschemer.Violation;

/**
 * A validator for XML files that are supposed to be valid according to the GeodesyML schemas.
 */
public class GeodesyMLValidator {

    private SchemaValidator schemaValidator;

    /**
     * Constructor, takes the root level GeodesyML XSD source and the path to the catalog file.
     * @param catalogPath full path to the catalog file, may be null
     */
    public GeodesyMLValidator(String catalogPath) {

        try {
            URL schemaUrl = ResourceUtils.getURL("classpath:xsd/geodesyml-0.1.0/geodesyml/0.4/geodesyML.xsd");
            Source xsdSource = new StreamSource(schemaUrl.openStream());
            schemaValidator = new SchemaValidator(xsdSource, catalogPath);
        }
        catch (IOException | SAXException e) {
            throw new GeodesyRuntimeException("Failed to initialise GeodesyMLValidator", e);
        }
    }

    private List<Violation> validateAgainstSchema(StreamSource xml) throws IOException {
        return schemaValidator.validate(xml);
    }

    public List<Violation> validate(StreamSource xml) throws IOException {
        return validateAgainstSchema(xml);
    }
}
