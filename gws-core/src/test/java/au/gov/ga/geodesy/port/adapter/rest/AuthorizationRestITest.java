package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.CorsNetworkRepository;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

/**
 * Test secure uploading of site logs.
 */
public class AuthorizationRestITest extends IntegrationTest {

    @Autowired
    private CorsNetworkRepository networks;

    private String alicSiteLog() throws IOException {
        return IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("ALIC"));
    }

    private MockMvcResponse isAuthorisedToUpload(String fourCharId) {
        return isAuthorisedToUpload(null, fourCharId);
    }

    private MockMvcResponse isAuthorisedToUpload(String jwtToken, String fourCharId) {
        MockMvcRequestSpecification request = given();
        if (jwtToken != null) {
            request = request.auth().with(bearerToken(jwtToken));
        }
        return request.queryParam("fourCharacterId", fourCharId).when().get("/siteLogs/isAuthorisedToUpload");
    }

    private MockMvcResponse upload(String fourCharId) throws IOException {
        return upload(null, fourCharId);
    }

    private MockMvcResponse upload(String jwtToken, String siteLog) throws IOException {
        MockMvcRequestSpecification request = given();
        if (jwtToken != null) {
            request = request.auth().with(bearerToken(jwtToken));
        }
        return request.body(alicSiteLog()).when().post("/siteLogs/upload");
    }

    @Test
    @Rollback(false)
    public void uploadUnauthenticated() throws Exception {
        isAuthorisedToUpload("ALIC").then().statusCode(HttpStatus.UNAUTHORIZED.value());
        upload(alicSiteLog()).then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Rollback(false)
    public void uploadUnauthorized() throws Exception {
        String jwt = jwtToken("edit-xxx", 60);
        isAuthorisedToUpload(jwt, "ALIC").then().statusCode(HttpStatus.FORBIDDEN.value());
        upload(jwt, alicSiteLog()).then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Rollback(false)
    public void uploadExpired() throws Exception {
        String jwt = expiredJwtToken("edit-alic");
        isAuthorisedToUpload(jwt, "ALIC").then().statusCode(HttpStatus.UNAUTHORIZED.value());
        upload(jwt, alicSiteLog()).then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Rollback(false)
    public void uploadAuthorizedBySite() throws Exception {
        String jwt = jwtToken("edit-alic", 60);
        isAuthorisedToUpload(jwt, "ALIC").then().statusCode(HttpStatus.OK.value());
        upload(jwt, alicSiteLog()).then().statusCode(HttpStatus.CREATED.value());
    }

    @Test(dependsOnMethods = {"uploadAuthorizedBySite"})
    @Rollback(false)
    public void addAlicToApref() throws Exception {
        String addToNetworkHref = given()
            .when()
            .get("/corsSites/search/findByFourCharacterId?id=ALIC")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getString("_links.addToNetwork.href");

        String addToNetworkPath = new URL(addToNetworkHref).getPath();

        given()
            .auth().with(super.superuserToken())
            .queryParam("networkId", networks.findByName("APREF").getId())
            .when()
            .put(addToNetworkPath)
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test(dependsOnMethods = {"addAlicToApref"})
    @Rollback(false)
    public void uploadAuthorizedByNetwork() throws Exception {
        String jwt = jwtToken("edit-network:apref", 60);
        isAuthorisedToUpload(jwt, "ALIC").then().statusCode(HttpStatus.OK.value());
        upload(jwt, alicSiteLog()).then().statusCode(HttpStatus.CREATED.value());
    }
}
