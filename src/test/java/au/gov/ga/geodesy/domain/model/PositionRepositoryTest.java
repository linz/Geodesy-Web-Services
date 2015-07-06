package au.gov.ga.geodesy.domain.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional("geodesyTransactionManager")
public class PositionRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private PositionRepository positions;

    @Test
    public void test() {
    }
}

