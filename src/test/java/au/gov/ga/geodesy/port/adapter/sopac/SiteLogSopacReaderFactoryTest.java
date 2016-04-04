package au.gov.ga.geodesy.port.adapter.sopac;

import static org.testng.Assert.assertTrue;

import java.io.FileReader;
import java.io.Reader;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.spring.GeodesySupportConfig;

@ContextConfiguration(
        classes = {GeodesySupportConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class SiteLogSopacReaderFactoryTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testInputRecognition() throws Exception {
        SiteLogSopacReaderFactory factory = new SiteLogSopacReaderFactory();
        try (Reader reader = new FileReader(ResourceUtils.getFile("classpath:sitelog/ALIC.xml"))) {
            assertTrue(factory.canRecogniseInput(reader));
        }
    }

    // TODO: test negative case
}
