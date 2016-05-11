package au.gov.ga.geodesy.port.adapter.geodesyml;

import au.gov.ga.xmlschemer.Violation;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.Test;

import javax.xml.transform.stream.StreamSource;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class GeodesyMLValidatorTest {

    // TODO fix the constructor so that the unit tests will pass
    private GeodesyMLValidator validator = new GeodesyMLValidator(null, "");

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
