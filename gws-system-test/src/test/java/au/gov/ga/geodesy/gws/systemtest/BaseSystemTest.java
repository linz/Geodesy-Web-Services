package au.gov.ga.geodesy.gws.systemtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

import au.gov.ga.geodesy.gws.support.spring.SystemTestConfig;

import io.restassured.RestAssured;
import io.restassured.specification.ProxySpecification;

/**
 * Base class for system tests.
 * Configures the web endpoints and clears the database prior to running each test.
 */
@ContextConfiguration(
    classes = {
        SystemTestConfig.class,
    },
    loader = AnnotationConfigContextLoader.class
)
public abstract class BaseSystemTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SystemTestConfig config;

    private static final Logger log = LoggerFactory.getLogger(BaseSystemTest.class);

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
    }

    protected SystemTestConfig getConfig() {
        return config;
    }
}
