package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;

public class CreateCorsNetworksSystemTest extends BaseSystemTest {

    @Test
    public void testUnauthorisedCreate() throws Exception {
        given()
            .header("Authorization", "Bearer " + super.userAToken())
            .contentType(ContentType.JSON)
            .body("{\"name\": \"IGS\"}")
            .when()
            .post(getConfig().getWebServicesUrl() + "/corsNetworks")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testUnauthorisedDelete() throws Exception {
        given()
            .header("Authorization", "Bearer " + super.userAToken())
            .delete(super.getConfig().getWebServicesUrl() + "/corsNetworks")
            .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void createCorsNetwork() throws Exception {
        String networkHref = given()
            .header("Authorization", "Bearer " + super.superuserToken())
            .contentType(ContentType.JSON)
            .body("{\"name\": \"TEST\"}")
            .when()
            .post(getConfig().getWebServicesUrl() + "/corsNetworks")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract().header("location");

        given()
            .header("Authorization", "Bearer " + super.superuserToken())
            .delete(networkHref)
            .then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}
