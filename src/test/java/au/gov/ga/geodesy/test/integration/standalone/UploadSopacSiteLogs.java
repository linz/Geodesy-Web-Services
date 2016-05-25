package au.gov.ga.geodesy.test.integration.standalone;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;

/**
 * Upload all SOPAC site logs.
 */
public class UploadSopacSiteLogs extends StandaloneIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(UploadSopacSiteLogs.class);

    private void uploadSopacSiteLog(File siteLog) throws Exception {
        log.info("Uploading " + siteLog.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .contentType("text/xml; charset=ISO-8859-1")
            .body(FileUtils.readFileToString(siteLog, "ISO-8859-1"))
        .when()
            .post(getConfig().getWebServicesUrl() + "/siteLog/sopac/upload")
        .then()
            .statusCode(201);
    }

    @Test
    public void upload() throws Exception {
        for (File siteLog : TestResources.sopacSiteLogs()) {
            uploadSopacSiteLog(siteLog);
        }
    }
}
