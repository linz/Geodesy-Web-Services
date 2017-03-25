package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.domain.model.UserRegistration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

public class UserRegistrationRestITest extends RestTest {

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        RestAssuredMockMvc.mockMvc(RestTest.mvc);
    }

    @Test
    @Rollback(false)
    public void createUserRegistration() throws Exception {
        given()
            .body(mapper.writeValueAsString(new UserRegistration(
                "Lazar",
                "Bodor",
                "Geoscience Australia",
                "Software developer",
                "lazar.bodor@ga.gov.au",
                "0451061798")))

            .when()
            .post("/userRegistrations")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
