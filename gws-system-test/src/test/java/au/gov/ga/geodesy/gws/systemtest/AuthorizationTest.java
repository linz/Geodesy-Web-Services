package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;

/**
 * System tests for secure upload of GeodesyML site log documents.
 */
public class AuthorizationTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationTest.class);

    /**
     * Authentication against OpenAM.
     *
     * @return JWT token
     */
    public String authenticate() {
       return given()
            .auth().preemptive().basic("gnssSiteManager", "gumby123")
            .contentType(ContentType.URLENC)
            .formParam("grant_type", "password")
            .formParam("username", "user1")
            .formParam("password", "gumby123")
            .formParam("scope", "openid profile")
            .param("realm", "/")
            .when()
            .post(getConfig().getOauthProviderUrl() + "/access_token")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().path("id_token").toString();
    }

    /**
     * Add the given base64-encoded JWT token to a mock HTTP servlet request.
     */
    public RequestPostProcessor bearerToken(String jwt) {
        return mockRequest -> {
            mockRequest.addHeader("Authorization", "Bearer " + jwt);
            return mockRequest;
        };
    }

    @Test
    public void uploadUnauthenticated() throws Exception {
        File alic = SystemTestResources.siteLog("alic*");
        log.info("Uploading " + alic.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .body(FileUtils.readFileToString(alic, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/secureUpload")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void uploadUnauthorized() throws Exception {
        File alic = SystemTestResources.siteLog("alic*");
        log.info("Uploading " + alic.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + authenticate())
            .body(FileUtils.readFileToString(alic, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/secureUpload")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void uploadAuthorized() throws Exception {
        File ade1 = SystemTestResources.siteLog("ade1*");
        log.info("Uploading " + ade1.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + authenticate())
            .body(FileUtils.readFileToString(ade1, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/secureUpload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
