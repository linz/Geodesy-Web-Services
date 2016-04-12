package au.gov.ga.geodesy.port.adapter.geodesyml;

import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.springframework.util.ResourceUtils;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class GeodesyMLValidatorTest {

    private GeodesyMLValidator validator = new GeodesyMLValidator();

    @Test
    public void testSchemaValidation() throws Exception {
        StreamSource xml = new StreamSource(ResourceUtils.getFile("classpath:sitelog/geodesyml/MOBS.xml"));
        List<String> violations = validator.validate(xml);
        assertNoViolations(violations);
    }

    private void assertNoViolations(List<String> violations) throws AssertionError {
        assertViolations(violations, 0);
    }

    private void assertViolations(List<String> violations, int n) throws AssertionError {
        if (n != violations.size()) {
            violations.forEach(v -> System.out.println(v));
        }
        assertEquals(violations.size(), n);
    }
}
