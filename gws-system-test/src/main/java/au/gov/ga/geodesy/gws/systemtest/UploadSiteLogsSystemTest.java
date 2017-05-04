package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

/**
 * System tests for SOPAC site logs.
 */
public class UploadSiteLogsSystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(UploadSiteLogsSystemTest.class);

    private void upload(Resource siteLog) throws Exception {
        log.info("Uploading " + siteLog.getURL() + " to " + getConfig().getWebServicesUrl());
        given()
            .header("Authorization", "Bearer " + super.superuserToken())
            .body(readResource(siteLog))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void uploadAllSiteLogs() throws Exception {
        for (Resource siteLog : SystemTestResources.siteLogs()) {
            upload(siteLog);
        }
    }
}
