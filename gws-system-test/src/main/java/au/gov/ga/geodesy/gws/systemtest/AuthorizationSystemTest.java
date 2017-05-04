package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

/**
 * System tests for secure upload of GeodesyML site log documents.
 */
public class AuthorizationSystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationSystemTest.class);

    @Test
    public void uploadUnauthenticated() throws Exception {
        Resource alic = SystemTestResources.siteLog("alic*");
        log.info("Uploading " + alic.getURL() + " to " + getConfig().getWebServicesUrl());
        given()
            .body(readResource(alic))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void uploadUnauthorized() throws Exception {
        Resource alic = SystemTestResources.siteLog("alic*");
        log.info("Uploading " + alic.getURL() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + super.userAToken())
            .body(readResource(alic))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void uploadAuthorized() throws Exception {
        Resource ade1 = SystemTestResources.siteLog("ade1*");
        log.info("Uploading " + ade1.getURL() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + super.userAToken())
            .body(readResource(ade1))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
