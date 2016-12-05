package au.gov.ga.geodesy.support.spring;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(
    classes = {
        GeodesySupportConfig.class,
    },
    loader = AnnotationConfigContextLoader.class)
public class UnitTest extends AbstractTestNGSpringContextTests {
}
