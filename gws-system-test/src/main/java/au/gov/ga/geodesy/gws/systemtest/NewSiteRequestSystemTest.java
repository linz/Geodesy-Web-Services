package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.domain.model.NewSiteRequest;
import io.restassured.http.ContentType;

public class NewSiteRequestSystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(NewSiteRequestSystemTest.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    @Rollback(false)
    public void createNewSiteRequest() throws Exception {
    	NewSiteRequest newSiteRequest = new NewSiteRequest(
            "Lazar",
            "Bodor",
            "Geoscience Australia",
            "Software developer",
            "lazar.bodor@ga.gov.au",
            "0451061798",
            getSiteLogContents());

        log.info("Creating new site request for " + newSiteRequest.getFirstName() + " " + newSiteRequest.getLastName());
        given()
            .contentType(ContentType.JSON)
            .body(mapper.writeValueAsString(newSiteRequest))
            .when()
            .post(getConfig().getWebServicesUrl() + "/newSiteRequests")
            .then()
            .statusCode(HttpStatus.CREATED.value());

        // TODO: find a way to receive the notification message from AWS
    }
    
    private String getSiteLogContents() throws Exception {
    	Resource siteLog = SystemTestResources.siteLog("newSite/new_site_ZZZZ");
    	return readResource(siteLog);
    }
}
