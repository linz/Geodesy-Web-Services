package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import static org.hamcrest.MatcherAssert.assertThat;


import static org.hamcrest.Matchers.equalTo;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import au.gov.ga.xmlschemer.Violation;

public class GeodesyMLValidatorTest  {

    private GeodesyMLValidator geodesyMLValidator;

    @BeforeTest
    public void setup() {
        
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext()) {
            Resource catalogResource = context.getResource("classpath:xsd/geodesyml-1.0.0-SNAPSHOT/catalog.xml");
            String catalogPath = catalogResource.getFile().getAbsolutePath();
            geodesyMLValidator = new GeodesyMLValidator(catalogPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSchemaValidation() throws Exception {
        StreamSource xml = new StreamSource(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS.xml"));
        List<Violation> violations = geodesyMLValidator.validate(xml);
        assertNoViolations(violations);
    }

    @Test
    public void testSchemaValidationFailure() throws Exception {
        StreamSource xml = new StreamSource(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS-invalid-schema.xml"));
        List<Violation> violations = geodesyMLValidator.validate(xml);
        assertViolations(violations, 2);
    }

    private void assertNoViolations(List<Violation> violations) throws AssertionError {
        assertViolations(violations, 0);
    }

    private void assertViolations(List<Violation> violations, int n) throws AssertionError {
        if (n != violations.size()) {
            violations.forEach(System.out::println);
        }
        assertThat(violations.size(), equalTo(n));
    }
}
