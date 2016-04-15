package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.springframework.util.ResourceUtils;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import au.gov.ga.xmlschemer.Violation;

public class GeodesyMLValidatorTest {

    private GeodesyMLValidator validator = new GeodesyMLValidator();

    @Test
    public void testSchemaValidation() throws Exception {
        StreamSource xml = new StreamSource(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS.xml"));
        List<Violation> violations = validator.validate(xml);
        assertNoViolations(violations);
    }

    private void assertNoViolations(List<Violation> violations) throws AssertionError {
        assertViolations(violations, 0);
    }

    private void assertViolations(List<Violation> violations, int n) throws AssertionError {
        if (n != violations.size()) {
            violations.forEach(System.out::println);
        }
        assertEquals(violations.size(), n);
    }
}
