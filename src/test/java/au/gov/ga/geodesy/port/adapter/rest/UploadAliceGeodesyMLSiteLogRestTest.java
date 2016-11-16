package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import static org.hamcrest.Matchers.is;

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
        String geodesyML = IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("ALIC"));
        given()
            .body(geodesyML).
        when()
            .post("/siteLogs/upload").
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

    @Test(dependsOnMethods = {"upload"})
    public void checkReceivers() throws Exception {
        given().
        when()
            .get("/gnssReceivers").
        then()
            .statusCode(200)
            .log().body()
            .body("page.totalElements", is(5))
            .body("_embedded.gnssReceivers[0].type", is("LEICA GRX1200GGPRO"))
            ;

    }

    // TODO: checkout uploaded site log, site, setups, equipment, and nodes
}
