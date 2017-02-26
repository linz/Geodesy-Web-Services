package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.ResourceServerConfig;
import au.gov.ga.geodesy.support.spring.ResourceServerTestConfig;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

/**
 * Test secure uploading of site logs.
 */
public class AuthorizationRestITest extends RestTest {

    @BeforeClass
    public void setup() {
        RestAssuredMockMvc.mockMvc(RestTest.mvc);
    }

    /**
     * Return base64-encoded JWT token with given authority and expiry period.
     */
    private String jwtToken(String authority, int period) {
        String signatureAlgorithm = "HS256";

        HashMap<String, String> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", signatureAlgorithm);
        header.put("exp", String.valueOf(new Date().getTime() + period));

        String claims = "{\"sub\": \"user\", \"authorities\": [\"" + authority + "\"]}";


        Jwt token = JwtHelper.encode(claims, ResourceServerTestConfig.macSigner, header);
        return token.getEncoded();
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

    private String alicSiteLog() throws IOException {
        return IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("ALIC"));
    }

    @Test
    @Rollback(false)
    public void uploadUnauthenticated() throws Exception {
        given()
            .body(alicSiteLog()).
        when()
            .post("/siteLogs/secureUpload").
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
            .post("/siteLogs/secureUpload").
        then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Rollback(false)
    public void uploadAuthorized() throws Exception {
        given()
            .auth().with(bearerToken(jwtToken("edit-alic", 60)))
            .body(alicSiteLog()).
        when()
            .post("/siteLogs/secureUpload").
        then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
