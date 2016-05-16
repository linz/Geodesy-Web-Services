package au.gov.ga.geodesy.support.spring;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

/**
 * Created by brookes on 16/05/2016.
 */

@ContextConfiguration(
        classes = {GeodesySupportConfig.class, GeodesyServiceUnitTestConfig.class, PersistenceJpaConfig.class,
                TestAppConfig.class },
        loader = AnnotationConfigContextLoader.class)
public class UnitTestConfig extends AbstractTransactionalTestNGSpringContextTests {
}
