package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndpointSecuritySystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(EndpointSecuritySystemTest.class);

    // TODO: test that the list is complete
    private static final String[] resourceCollections = new String[] {
        "/equipmentConfigurations",
        "/events",
        "/positions",
        "/weeklySolutions",
        "/newCorsSiteRequests",
        "/corsSiteInNetworks",
        // "/corsNetworks", // TODO: POST, PUT, PATCH, and DELETE are allowed for superuser
        "/userRegistrations",
        "/contactTypes",
        "/setups",
        "/nodes",
        "/gnssReceivers",
        "/siteLogs",
        // "/siteLogs/upload", // TODO: POST is allowed for authenticated users
        "/corsSites",
        "/gnssAntennas",
        "/equipment",
    };

    private RequestSpecification[] requests;

    @BeforeClass
    public void setup() {
        requests = new RequestSpecification[] {
            given().when(), // unauthenticated
            given().header("Authorization", "Bearer " + super.superuserToken()).when(),
        };
    }

    @Test
    public void testReadonlyEndpoints() {
        Arrays.stream(resourceCollections)
            .map(resources -> getConfig().getWebServicesUrl() + resources)
            .forEach(resources -> {
                log.info("Checking " + resources);

                testPostDenied(resources);
                log.info("POST is denied");

                String resource = resources + "/0";

                testPutDenied(resource);
                log.info("PUT is denied");

                testPatchDenied(resource);
                log.info("PATCH is denied");

                testDeleteDenied(resource);
                log.info("DELETE is denied");
            });
    }

    private void testPostDenied(String url) {
        Arrays.stream(requests).forEach(request -> 
            assertDenied(request.post(url))
        );
    }

    private void testPutDenied(String url) {
        Arrays.stream(requests).forEach(request -> 
            assertDenied(request.put(url))
        );
    }

    private void testPatchDenied(String url) {
        Arrays.stream(requests).forEach(request -> 
            assertDenied(request.patch(url))
        );
    }

    private void testDeleteDenied(String url) {
        Arrays.stream(requests).forEach(request -> 
            assertDenied(request.delete(url))
        );
    }

    private void assertDenied(Response response) {
        assertThat(response.statusCode(), anyOf(is(HttpStatus.UNAUTHORIZED.value()), is(HttpStatus.FORBIDDEN.value())));
    }
}
