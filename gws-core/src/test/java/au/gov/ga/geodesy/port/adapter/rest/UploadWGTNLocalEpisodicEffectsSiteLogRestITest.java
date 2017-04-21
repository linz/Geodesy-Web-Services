package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.config.XmlConfig.xmlConfig;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

public class UploadWGTNLocalEpisodicEffectsSiteLogRestITest extends IntegrationTest {

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        String geodesyML = IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("WGTN-localEpisodicEffects"));
        given()
            .auth().with(bearerToken(jwtToken("edit-wgtn", 60)))
            .body(geodesyML).
        when()
            .post("/siteLogs/upload").
        then()
            .statusCode(201);
    }

    // test the GeodesyML response, expecting 4 localEpisodicEffects nodes
    @Test(dependsOnMethods = "upload")
    public void checkLocalEpisodicEffectsGeodesyML() throws Exception {
        given()
            .config(RestAssuredMockMvc.config().xmlConfig(xmlConfig().declareNamespace("geo", "urn:xml-gov-au:icsm:egeodesy:0.4")))
            .when()
            .get("/siteLogs/search/findByFourCharacterId?id=WGTN&format=geodesyml")
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/xml")
                .log().body()
                .body("geo:siteLog.geo:localEpisodicEffect.size()", equalTo(4));
    }
}
