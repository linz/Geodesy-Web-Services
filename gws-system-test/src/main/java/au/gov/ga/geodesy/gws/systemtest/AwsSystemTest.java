package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.aws.Aws;

import io.restassured.response.ValidatableResponse;

public class AwsSystemTest extends BaseSystemTest {

    @Test
    public void testGetStackName() {

        Aws aws = new Aws();

        ValidatableResponse response = given()
            .when()
            .get(getConfig().getWebServicesUrl() + "/aws/stackName")
            .then();

        if (aws.inAmazon()) {
            String stackName = response
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();

            assertThat(stackName, is("DevGeodesy"));
        } else {
            response.statusCode(HttpStatus.NO_CONTENT.value());
        }
    }
}

