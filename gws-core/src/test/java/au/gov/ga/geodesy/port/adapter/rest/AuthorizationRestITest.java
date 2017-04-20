package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

/**
 * Test secure uploading of site logs.
 */
public class AuthorizationRestITest extends RestTest {

    @BeforeClass
    public void setup() {
        RestAssuredMockMvc.mockMvc(RestTest.mvc);
    }

    private String alicSiteLog() throws IOException {
        return IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("ALIC"));
    }

    @Test
    @Rollback(false)
    public void uploadUnauthenticated() throws Exception {
        given()
            .body(alicSiteLog()).
        when()
            .post("/siteLogs/upload").
        then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Rollback(false)
    public void uploadUnauthorized() throws Exception {
        given()
            .auth().with(bearerToken(jwtToken("edit-xxx", 60)))
            .body(alicSiteLog()).
        when()
            .post("/siteLogs/upload").
        then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Rollback(false)
    public void uploadExpired() throws Exception {
        given()
            .auth().with(bearerToken(expiredJwtToken("edit-alic")))
            .body(alicSiteLog()).
        when()
            .post("/siteLogs/upload").
        then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Rollback(false)
    public void uploadAuthorized() throws Exception {
        given()
            .auth().with(bearerToken(jwtToken("edit-alic", 60)))
            .body(alicSiteLog()).
        when()
            .post("/siteLogs/upload").
        then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
