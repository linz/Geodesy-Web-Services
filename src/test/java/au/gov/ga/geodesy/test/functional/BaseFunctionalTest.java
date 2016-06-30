package au.gov.ga.geodesy.test.functional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.support.spring.FunctionalTestConfig;
import au.gov.ga.geodesy.support.spring.PersistenceJpaConfig;
import io.restassured.RestAssured;
import io.restassured.specification.ProxySpecification;

/**
 * Base class for functional tests.
 * Configures the web endpoints and clears the database prior to running each test.
 */
@ContextConfiguration(
    classes = {
        FunctionalTestConfig.class,
        PersistenceJpaConfig.class
    },
    loader = AnnotationConfigContextLoader.class
)
public abstract class BaseFunctionalTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private FunctionalTestConfig config;

    @Autowired
    protected SiteLogRepository siteLogRepository;

    private static final Logger log = LoggerFactory.getLogger(BaseFunctionalTest.class);

    @BeforeClass
    public void setup() {
        if (config.getUseProxy()) {
            RestAssured.proxy = ProxySpecification
                .host(config.getHttpProxyHost())
                .withPort(config.getHttpProxyPort())
                .withAuth(config.getHttpProxyUser(), config.getHttpProxyPassword());

            log.info("Using HTTP proxy http://" + config.getHttpProxyUser() + ":xxx@"
                    + config.getHttpProxyHost() + ":" + config.getHttpProxyPort());
        } else {
            log.info("HTTP proxy is not configured");
        }

        siteLogRepository.deleteAll();
        siteLogRepository.flush();
    }

    protected FunctionalTestConfig getConfig() {
        return config;
    }
}
