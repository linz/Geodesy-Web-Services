package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

/**
 * System tests for SOPAC site logs.
 */
public class UploadSiteLogsSystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(UploadSiteLogsSystemTest.class);

    private void upload(File siteLog) throws Exception {
        log.info("Uploading " + siteLog.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .body(FileUtils.readFileToString(siteLog, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void uploadAllSiteLogs() throws Exception {
        for (File siteLog : SystemTestResources.siteLogs()) {
            upload(siteLog);
        }
    }
}
