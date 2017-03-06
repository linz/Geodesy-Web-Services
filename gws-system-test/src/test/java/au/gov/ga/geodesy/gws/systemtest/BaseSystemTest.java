package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testng.annotations.BeforeClass;

import au.gov.ga.geodesy.gws.support.spring.SystemTestConfig;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

    /**
     * Authentication against OpenAM.
     *
     * @return JWT token
     */
    protected String authenticate(String username, String password) {
       return given()
            .auth().preemptive().basic("GnssSiteManager", "gumby123")
            .contentType(ContentType.URLENC)
            .formParam("grant_type", "password")
            .formParam("username", username)
            .formParam("password", password)
            .formParam("scope", "openid profile")
            .param("realm", "/")
            .when()
            .post(getConfig().getOauthProviderUrl() + "/access_token")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().path("id_token").toString();
    }

    protected String userAToken() {
        return authenticate("user.a", "gumby123A");
    }

    protected String superuserToken() {
        return authenticate("user.x", "gumby123X");
    }

    protected SystemTestConfig getConfig() {
        return config;
    }
}
