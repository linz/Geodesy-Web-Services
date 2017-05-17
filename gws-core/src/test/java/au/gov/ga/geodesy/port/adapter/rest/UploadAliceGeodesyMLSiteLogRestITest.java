package au.gov.ga.geodesy.port.adapter.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import au.gov.ga.geodesy.domain.model.ContactType;
import au.gov.ga.geodesy.domain.model.ContactTypeRepository;
import au.gov.ga.geodesy.domain.model.SiteResponsibleParty;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import au.gov.ga.geodesy.domain.model.sitelog.SiteLogRepository;
import au.gov.ga.geodesy.support.TestResources;
import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class UploadAliceGeodesyMLSiteLogRestITest extends IntegrationTest {

    @Autowired
    private SiteLogRepository siteLogs;

    @Autowired
    private ContactTypeRepository contactTypes;

    @Test
    @Rollback(false)
    public void upload() throws Exception {
        String geodesyML = IOUtils.toString(TestResources.customGeodesyMLSiteLogReader("ALIC"));
        given()
            .auth().with(super.superuserToken())
            .body(geodesyML).
        when()
            .post("/siteLogs/upload").
        then()
            .statusCode(201);
    }

    @Test(dependsOnMethods = {"upload"})
    public void checkUploaded() throws Exception {
        given().
        when()
            .get("/corsSites/search/findByFourCharacterId?id=ALIC").
        then()
            .statusCode(200)
            .log().body();
    }

    @Test(dependsOnMethods = {"upload"})
    public void checkReceivers() throws Exception {
        given().
        when()
            .get("/gnssReceivers").
        then()
            .statusCode(200)
            .log().body()
            .body("page.totalElements", is(6))
            ;
    }

    private void checkResponsiblePartiesByType(SiteLog siteLog, ContactType contactType, List<String> individualNames) {
        checkResponsibleParties(siteLog.getResponsiblePartiesByType(contactType), individualNames);
    }

    private void checkResponsibleParties(List<SiteResponsibleParty> responsibleParties, List<String> individualNames) {
        assertThat(responsibleParties.stream().map(p -> p.getParty().getIndividualName()).collect(Collectors.toList()),
            is(equalTo(individualNames)));
    }

    @Test(dependsOnMethods = {"upload"})
    public void checkResponsibleParties() throws Exception {
        SiteLog alice = siteLogs.findByFourCharacterId("ALIC");

        LinkedHashMap<ContactType, List<String>> expectedResponsibleParties = new LinkedHashMap<>();
        expectedResponsibleParties.put(contactTypes.siteOwner(), Lists.newArrayList("Site Owner"));
        expectedResponsibleParties.put(contactTypes.siteContact(), Lists.newArrayList("Site Contact 1", "Site Contact 2"));
        expectedResponsibleParties.put(contactTypes.siteMetadataCustodian(), Lists.newArrayList("Site Metadata Custodian"));
        expectedResponsibleParties.put(contactTypes.siteDataCenter(), Lists.newArrayList("Site Data Center 1", "Site Data Center 2"));
        expectedResponsibleParties.put(contactTypes.siteDataSource(), Lists.newArrayList("Site Data Source"));

        List<String> allIndividualNames = new ArrayList<>();
        for (Entry<ContactType, List<String>> e : expectedResponsibleParties.entrySet()) {
            checkResponsiblePartiesByType(alice, e.getKey(), e.getValue());
            allIndividualNames.addAll(e.getValue());
        }
        checkResponsibleParties(alice.getResponsibleParties(), allIndividualNames);
    }

    // TODO: checkout uploaded site log, site, setups, equipment, and nodes
}
