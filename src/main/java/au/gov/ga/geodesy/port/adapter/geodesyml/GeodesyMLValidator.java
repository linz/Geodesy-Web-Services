package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import au.gov.ga.xmlschemer.SchemaValidator;

@Component
public class GeodesyMLValidator {

    private SchemaValidator schemaValidator;

    public GeodesyMLValidator() {
        try {
            Source xsd = new StreamSource(ResourceUtils.getFile("classpath:xsd/geodesyml-1.0.0-SNAPSHOT/geodesyML.xsd"));
            String catalog = ResourceUtils.getFile("classpath:xsd/geodesyml-1.0.0-SNAPSHOT/third-party/catalog.xml").getAbsolutePath();
            schemaValidator = new SchemaValidator(xsd, catalog);
        }
        catch (FileNotFoundException | SAXException e) {
            throw new RuntimeException("Failed to initialise GeodesyMLValidator", e);
        }
    }

    private List<String> validateAgainstSchema(StreamSource xml) throws IOException {
        return schemaValidator.validate(xml);
    }

    public List<String> validate(StreamSource xml) throws IOException {
        return validateAgainstSchema(xml);
    }
}
