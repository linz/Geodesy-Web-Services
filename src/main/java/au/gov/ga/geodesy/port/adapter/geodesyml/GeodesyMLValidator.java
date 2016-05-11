package au.gov.ga.geodesy.port.adapter.geodesyml;

import au.gov.ga.xmlschemer.SchemaValidator;
import au.gov.ga.xmlschemer.Violation;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

/**
 * A validator for XML files that are supposed to be valid according to the GeodesyML schemas.
 */
public class GeodesyMLValidator {

    private SchemaValidator schemaValidator;

    /**
     * Constructor, takes the root level XSD source and the path to the catalog file.
     * @param xsdSource
     * @param catalogPath
     */
    public GeodesyMLValidator(Source xsdSource, String catalogPath) {
        try {
            schemaValidator = new SchemaValidator(xsdSource, catalogPath);
        }
        catch (SAXException e) {
            throw new RuntimeException("Failed to initialise GeodesyMLValidator", e);
        }
    }

    private List<Violation> validateAgainstSchema(StreamSource xml) throws IOException {
        return schemaValidator.validate(xml);
    }

    public List<Violation> validate(StreamSource xml) throws IOException {
        return validateAgainstSchema(xml);
    }
}
