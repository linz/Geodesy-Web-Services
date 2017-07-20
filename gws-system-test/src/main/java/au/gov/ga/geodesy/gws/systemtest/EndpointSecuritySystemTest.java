package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
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
        // "/newCorsSiteRequests", // TODO: POST is allowed for authenticated users
        "/corsSiteInNetworks",
        // "/corsNetworks", // TODO: POST, PUT, PATCH, and DELETE are allowed for superuser
        // "/userRegistrations", // TODO: POST is allowed for unauthenticated users
        "/contactTypes",
        "/setups",
        "/nodes",
        "/gnssReceivers",
        "/siteLogs",
        // "/siteLogs/upload", // TODO: POST is allowed for authenticated users
        // "/corsSites", // TODO: PATCH is allowed for superuser
        "/gnssAntennas",
        "/equipment",
    };

    private List<Pair<String, RequestSpecification>> requests;

    @BeforeClass
    public void setup() {
        requests = Stream.of(
            Pair.of("unauthenticated", given().when()),
            Pair.of("as superuser", given().header("Authorization", "Bearer " + super.superuserToken()).when())
        ).collect(Collectors.toList());
    }

    @Test
    public void testReadonlyEndpoints() {
        Arrays.stream(resourceCollections)
            .map(resources -> getConfig().getWebServicesUrl() + resources)
            .forEach(resources -> {
                testPostDenied(resources);

                String resource = resources + "/0";

                testPutDenied(resource);
                testPatchDenied(resource);
                testDeleteDenied(resource);
            });
    }

    private void testPostDenied(String url) {
        requests.forEach(request -> {
            log.info("POST to " + url + " " + request.getLeft());
            assertDenied(request.getRight().post(url));
            log.info("Denied");
        });
    }

    private void testPutDenied(String url) {
        requests.forEach(request -> {
            log.info("PUT to " + url + " " + request.getLeft());
            assertDenied(request.getRight().put(url));
            log.info("Denied");
        });
    }

    private void testPatchDenied(String url) {
        requests.forEach(request -> {
            log.info("PATCH to " + url + " " + request.getLeft());
            assertDenied(request.getRight().patch(url));
            log.info("Denied");
        });
    }

    private void testDeleteDenied(String url) {
        requests.forEach(request -> {
            log.info("DELETE to " + url + " " + request.getLeft());
            assertDenied(request.getRight().delete(url));
            log.info("Denied");
        });
    }

    private void assertDenied(Response response) {
        assertThat(response.statusCode(), anyOf(is(HttpStatus.UNAUTHORIZED.value()), is(HttpStatus.FORBIDDEN.value())));
    }
}
