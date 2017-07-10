package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.URL;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.domain.model.CorsNetworkRepository;
import au.gov.ga.geodesy.domain.model.NetworkTenancy;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.service.CorsSiteLogService;
import au.gov.ga.geodesy.port.adapter.sopac.SopacSiteLogReader;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class CorsSiteEndpointITest extends IntegrationTest {

    @Autowired
    private CorsSiteLogService siteLogService;

    @Autowired
    private CorsNetworkRepository networks;

    private Integer gpsNetworkId;

    @BeforeClass
    public void setup() {
        this.gpsNetworkId = networks.findByName("GPSNET").getId();
    }

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        SiteLog alice = new SopacSiteLogReader(TestResources.originalSopacSiteLogReader("ALIC")).getSiteLog();
        siteLogService.upload(alice);
    }

    @Test(dependsOnMethods = {"upload"})
    @Rollback(false)
    public void addSiteToNetwork() throws Exception {
        String addToNetworkHref = given()
            .when()
            .get("/corsSites/search/findByFourCharacterId?id=ALIC")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getString("_links.addToNetwork.href");

        String addToNetworkPath = new URL(addToNetworkHref).getPath();

        given()
            .auth().with(super.superuserToken())
            .queryParam("networkId", gpsNetworkId)
            .queryParam("effectiveFrom", "2011-12-12")
            .queryParam("timeFormat", "uuuu-MM-dd")
            .when()
            .put(addToNetworkPath)
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test(dependsOnMethods = {"addSiteToNetwork"})
    @Rollback(false)
    public void checkNetworkTenancy() throws Exception {
        List<NetworkTenancy> networkTenancies = given()
            .when()
            .get("/corsSites/search/findByFourCharacterId?id=ALIC")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList("networkTenancies", NetworkTenancy.class);

        assertThat(networkTenancies.size(), is(1));
        assertThat(networkTenancies.get(0).getCorsNetworkId(), is(this.gpsNetworkId));
        assertThat(networkTenancies.get(0).getPeriod().getFrom(), is(equalTo(Instant.parse("2011-12-12T00:00:00Z"))));
    }
}
