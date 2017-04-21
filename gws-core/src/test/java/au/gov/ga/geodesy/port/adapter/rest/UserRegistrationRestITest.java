package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.domain.model.UserRegistration;
import au.gov.ga.geodesy.port.adapter.mock.InMemoryNotificationAdapter;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class UserRegistrationRestITest extends IntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private InMemoryNotificationAdapter notificationAdapter;

    @Test
    @Rollback(false)
    public void createUserRegistration() throws Exception {

        assertThat(notificationAdapter.getNotifications().size(), is(0));

        given()
            .body(mapper.writeValueAsString(new UserRegistration(
                "Lazar",
                "Bodor",
                "Geoscience Australia",
                "Software developer",
                "lazar.bodor@ga.gov.au",
                "0451061798",
                "Could you please grant me write access for sites ADE1 and ADE2?")))

            .when()
            .post("/userRegistrations")
            .then()
            .statusCode(HttpStatus.CREATED.value());

        assertThat(notificationAdapter.getNotifications().size(), is(1));
        assertThat(notificationAdapter.getNotifications().get(0).getSubject(), is("UserRegistrationReceived"));
    }
}
