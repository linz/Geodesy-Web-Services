package au.gov.ga.geodesy.test.system;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.core.Is;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.TestResources;

/**
 * System tests for SOPAC site logs.
 */
public class SopacSiteLogsTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(SopacSiteLogsTest.class);

    private void uploadSopacSiteLog(File siteLog) throws Exception {
        log.info("Uploading " + siteLog.getName() + " to " + getConfig().getWebServicesUrl());
        given()
            .body(FileUtils.readFileToString(siteLog, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLog/sopac/upload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void uploadAllSiteLogs() throws Exception {

        int existingSitelogCount = getSiteLogCount();

        List<File> siteLogs = TestResources.sopacSiteLogs();
        for (File siteLog : siteLogs) {
            uploadSopacSiteLog(siteLog);
        }

        assertThat(getSiteLogCount(), Is.is(existingSitelogCount + siteLogs.size()));
    }

    @Test
    public void uploadAAAA() throws Exception {
        File siteLog = TestResources.sopacSiteLogTestData("ALIC-testUploadAAAA");
        int existingSitelogCount = getSiteLogCount();
        uploadSopacSiteLog(siteLog);
        log.info("Checking whether AAAA sitelog was successfully uploaded");
        given()
            .when()
            .get(getConfig().getWebServicesUrl() + "/siteLogs/search/findByFourCharacterId?id=AAAA")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("siteIdentification.siteName", equalTo("Test file based on ALIC"));
       assertThat(getSiteLogCount(), Is.is(existingSitelogCount + 1));
    }

    @Test
    public void validateValidSopacFile() throws Exception {

        File siteLog = TestResources.sopacSiteLog("MOBS");
        given()
            .body(FileUtils.readFileToString(siteLog, "ISO-8859-1"))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLog/sopac/upload")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void validateInvalidSopacFile() throws Exception {
        given()
            .body("!!!INVALID XML BODY!!!")
            .when().post(getConfig().getWebServicesUrl() + "/siteLog/sopac/upload")
            .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Gets the number of siteLogs in the repository.
     * @return
     */
    private int getSiteLogCount() {
        int totalElements =
            given()
                .when()
                .get(getConfig().getWebServicesUrl() + "/siteLogs")
                .then()
                .extract()
                .path("page.totalElements");

        return totalElements;
    }
}
