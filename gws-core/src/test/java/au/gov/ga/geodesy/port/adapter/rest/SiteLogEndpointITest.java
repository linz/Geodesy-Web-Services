package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.config.XmlConfig.xmlConfig;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

public class SiteLogEndpointITest extends IntegrationTest {

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        String geodesyML = IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("ALIC"));
        given()
            .auth().with(super.superuserToken())
            .body(geodesyML).
        when()
            .post("/siteLogs/upload").
        then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test(dependsOnMethods = "upload")
    @Rollback(false)
    public void testFindGeodesyMLSiteLog() throws Exception {
        given()
            .config(RestAssuredMockMvc.config().xmlConfig(xmlConfig().declareNamespace("geo", "urn:xml-gov-au:icsm:egeodesy:0.4")))
            .when()
            .get("/siteLogs/search/findByFourCharacterId?id=ALIC&format=geodesyml")
            .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/xml")
                .body("geo:GeodesyML.geo:siteLog.geo:siteIdentification.geo:siteName.text()", equalTo("Alice Springs AU012"))
                .apply(document("findSiteLogByFourCharacterId"));
    }

    @Test(dependsOnMethods = "upload")
    @Rollback(false)
    public void testFindJsonSiteLog() throws Exception {
        given()
            .when()
            .get("/siteLogs/search/findByFourCharacterId?id=ALIC&format=json")
            .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("siteIdentification.siteName", equalTo("Alice Springs AU012"));
    }

    @Test(dependsOnMethods = "upload")
    @Rollback(false)
    public void testDatePreparedProjection() throws Exception {
        given()
            .when()
            .get("/siteLogs/search/findByFourCharacterId?id=ALIC&format=json&projection=datePrepared")
            .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("fourCharacterId", equalTo("ALIC"))
                .body("datePrepared", equalTo("2017-02-23T00:00:00Z"))
                .body("lastModifiedDate", equalTo("1970-01-01T00:00:00Z"));
   }

    @Test
    public void testNotFound() throws Exception {
        given()
            .when()
            .get("/siteLogs/search/findByFourCharacterId?id=FOO&format=geodesyml")
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
