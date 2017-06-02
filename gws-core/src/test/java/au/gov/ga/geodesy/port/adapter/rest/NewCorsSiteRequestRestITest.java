package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.domain.model.NewCorsSiteRequest;
import au.gov.ga.geodesy.port.adapter.mock.InMemoryNotificationAdapter;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class NewCorsSiteRequestRestITest extends IntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private InMemoryNotificationAdapter notificationAdapter;

    @Test
    @Rollback(false)
    public void createNewCorsSiteRequest() throws Exception {

        assertThat(notificationAdapter.getNotifications().size(), is(0));

        String geodesyML = IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("new_site_ZZZZ"));
        
        given()
            .body(mapper.writeValueAsString(new NewCorsSiteRequest(
                "Lazar",
                "Bodor",
                "Geoscience Australia",
                "Software developer",
                "lazar.bodor@ga.gov.au",
                "0451061798",
                geodesyML)))

            .when()
            .post("/newCorsSiteRequests")
            .then()
            .statusCode(HttpStatus.CREATED.value());

        assertThat(notificationAdapter.getNotifications().size(), is(1));
        assertThat(notificationAdapter.getNotifications().get(0).getSubject(), is("NewCorsSiteRequestReceived"));
    }
}
