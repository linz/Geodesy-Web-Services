package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class SetupSystemTest extends BaseSystemTest {

    @Test
    public void findSetupsByIdAndDate() throws Exception {
        Response response = given()
            .get(getConfig().getWebServicesUrl() + "/setups/search/findByFourCharacterId?id=ALIC&effectiveFrom=2011-12-12&effectiveTo=2014-12-12&timeFormat=uuuu-MM-dd")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().response();

       assertThat(response.jsonPath().get("page.totalElements"), is(3));
       assertThat(response.jsonPath().get("_embedded.setups[0].name"), is("GNSS CORS Setup"));
       assertThat(response.jsonPath().get("_embedded.setups[0].name"), is("GNSS CORS Setup"));
       assertThat(response.jsonPath().get("_embedded.setups[0].effectivePeriod.from"), is("2011-07-20T00:00:00Z"));
       assertThat(response.jsonPath().get("_embedded.setups[0].effectivePeriod.to"), is("2013-03-08T00:00:00Z"));
    }
}
