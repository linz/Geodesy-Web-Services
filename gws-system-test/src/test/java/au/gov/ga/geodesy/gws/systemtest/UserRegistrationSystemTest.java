package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.domain.model.UserRegistration;

import io.restassured.http.ContentType;

public class UserRegistrationSystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationSystemTest.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    @Rollback(false)
    public void createUserRegistration() throws Exception {
        UserRegistration registration = new UserRegistration(
            "Lazar",
            "Bodor",
            "Geoscience Australia",
            "Software developer",
            "lazar.bodor@ga.gov.au",
            "0451061798");

        log.info("Creating new user registration for " + registration.getFirstName() + " " + registration.getLastName());
        given()
            .contentType(ContentType.JSON)
            .body(mapper.writeValueAsString(registration))
            .when()
            .post(getConfig().getWebServicesUrl() + "/userRegistrations")
            .then()
            .statusCode(HttpStatus.CREATED.value());

        // TODO: find a way to receive the notification message from AWS
    }
}
