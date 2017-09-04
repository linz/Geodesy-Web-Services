package au.gov.ga.geodesy.gws.systemtest;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.testng.annotations.Test;

/**
 * System tests for SOPAC site logs.
 */
public class UploadSiteLogsSystemTest extends BaseSystemTest {

    private static final Logger log = LoggerFactory.getLogger(UploadSiteLogsSystemTest.class);

    private int upload(Resource siteLog) throws Exception {
        // log.info("Uploading " + siteLog.getURL() + " to " + getConfig().getWebServicesUrl());
        return given()
            .header("Authorization", "Bearer " + super.superuserToken())
            .body(readResource(siteLog))
            .when()
            .post(getConfig().getWebServicesUrl() + "/siteLogs/upload")
            .then()
            .extract().statusCode();
            // .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void uploadAllSiteLogs() throws Exception {
        int i = 0;
        List<String> failed = new ArrayList<>();
        for (Resource siteLog : SystemTestResources.siteLogs()) {
            try {
                int statusCode = upload(siteLog);
                if (statusCode != 201) {
                    String message = "Failed: " + siteLog.getFilename();
                    System.out.println(message);
                    failed.add(message);
                }
                System.out.println(i++);
            }
            catch (Exception e) {
                log.error("Failed to upload " + siteLog.getFilename());
                e.printStackTrace();
            }
        }
        failed.forEach(System.out::println);
    }
}
