package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.spring.ResourceServerConfig;

/**
 * System tests for secure upload of GeodesyML site log documents.
 */
public class AuthorizationTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationTest.class);

    /**
     * Return base64-encoded JWT token with given authority and expiry period.
     */
    private String jwt(String authority, int period) {
        String signatureAlgorithm = "HS256";

        HashMap<String, String> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", signatureAlgorithm);
        header.put("exp", String.valueOf(new Date().getTime() + period));

        String claims = "{\"sub\": \"user\", \"authorities\": [\"" + authority + "\"]}";

        Jwt token = JwtHelper.encode(claims, ResourceServerConfig.signer, header);
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
            .header("Authorization", "Bearer " + jwt("edit-alicx", 60))
            .body(FileUtils.readFileToString(alic, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/secureUpload")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void uploadAuthorized() throws Exception {
        File alic = SystemTestResources.siteLog("alic*");
        log.info("Uploading " + alic.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + jwt("edit-alic", 60))
            .body(FileUtils.readFileToString(alic, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/secureUpload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
