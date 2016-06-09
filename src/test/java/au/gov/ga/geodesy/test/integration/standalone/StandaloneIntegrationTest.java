package au.gov.ga.geodesy.test.integration.standalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.specification.ProxySpecification;

import au.gov.ga.geodesy.support.spring.StandaloneIntegrationTestConfig;

/**
 * Base class for standalone integration tests.
 */
@ContextConfiguration(
    classes = {
        StandaloneIntegrationTestConfig.class,
    },
    loader = AnnotationConfigContextLoader.class
)
public class StandaloneIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private StandaloneIntegrationTestConfig config;

    private static final Logger log = LoggerFactory.getLogger(StandaloneIntegrationTest.class);

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

    protected StandaloneIntegrationTestConfig getConfig() {
        return config;
    }
}
