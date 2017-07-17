package au.gov.ga.geodesy.support.spring;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(
    classes = {
        UnitTestConfig.class,
    },
    loader = AnnotationConfigContextLoader.class
)
public class UnitTest extends AbstractTestNGSpringContextTests {
}
