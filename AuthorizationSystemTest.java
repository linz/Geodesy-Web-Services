package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

/**
 * System tests for secure upload of GeodesyML site log documents.
 */
public class AuthorizationSystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationSystemTest.class);

    @Test
    public void uploadUnauthenticated() throws Exception {
        File alic = SystemTestResources.siteLog("alic*");
        log.info("Uploading " + alic.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .body(FileUtils.readFileToString(alic, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void uploadUnauthorized() throws Exception {
        File alic = SystemTestResources.siteLog("alic*");
        log.info("Uploading " + alic.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + super.userAToken())
            .body(FileUtils.readFileToString(alic, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void uploadAuthorized() throws Exception {
        File ade1 = SystemTestResources.siteLog("ade1*");
        log.info("Uploading " + ade1.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + super.userAToken())
            .body(FileUtils.readFileToString(ade1, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
