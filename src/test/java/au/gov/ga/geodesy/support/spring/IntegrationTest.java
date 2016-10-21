package au.gov.ga.geodesy.support.spring;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;

import au.gov.ga.geodesy.domain.model.Repositories;

@ContextConfiguration(
        classes = {
                GeodesySupportConfig.class,
                GeodesyServiceTestConfig.class,
                PersistenceJpaConfig.class,
        },
        loader = AnnotationConfigContextLoader.class)
public class IntegrationTest extends AbstractTransactionalTestNGSpringContextTests {

    @AfterClass(alwaysRun = true)
    @Rollback(false)
    protected void deleteData() {
        new Repositories().deleteAll();
    }
}
