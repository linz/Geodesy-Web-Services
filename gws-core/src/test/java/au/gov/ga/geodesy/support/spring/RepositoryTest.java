package au.gov.ga.geodesy.support.spring;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;

import au.gov.ga.geodesy.domain.model.Repositories;

@ContextConfiguration(
    classes = {
        GeodesySupportConfig.class,
        IntegrationTestConfig.class,
        PersistenceJpaConfig.class,
    },
    loader = AnnotationConfigContextLoader.class
)
@Transactional("geodesyTransactionManager")
public class RepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    @AfterClass(alwaysRun = true)
    @Rollback(false)
    protected void deleteData() {
        new Repositories().deleteAll();
    }

}
