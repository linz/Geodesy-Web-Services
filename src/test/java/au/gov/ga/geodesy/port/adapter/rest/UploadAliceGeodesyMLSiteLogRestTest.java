package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import org.apache.commons.io.IOUtils;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

public class UploadAliceGeodesyMLSiteLogRestTest extends RestTest {

    @BeforeClass
    public void setup() {
        RestAssuredMockMvc.mockMvc(RestTest.mvc);
    }

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        String geodesyML = IOUtils.toString(TestResources.geodesyMLSiteLogReader("ALIC"));
        given()
            .body(geodesyML).
        when()
            .post("/siteLog/upload").
        then()
            .statusCode(201);
    }

    @Test(dependsOnMethods = {"upload"})
    public void check() throws Exception {
        given().
        when()
            .get("/corsSites/search/findByFourCharacterId?id=ALIC").
        then()
            .statusCode(200)
            .log().body();
    }

    // TODO: checkout uploaded site log, site, setups, equipment, and nodes
}
