package au.gov.ga.geodesy.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

import au.gov.ga.geodesy.support.spring.GeodesyRepositoryRestMvcConfig;
import au.gov.ga.geodesy.support.spring.GeodesyRestMvcConfig;
import au.gov.ga.geodesy.support.spring.GeodesyServiceTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;

@ContextConfiguration(
        classes = {
            GeodesyServiceTestConfig.class,
            GeodesyRepositoryRestMvcConfig.class,
            GeodesyRestMvcConfig.class,
            PersistenceJpaConfig.class
        },
        loader = AnnotationConfigWebContextLoader.class)

@WebAppConfiguration
@Transactional("geodesyTransactionManager")
public class RestTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected static MockMvc mvc;

    @BeforeClass
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
