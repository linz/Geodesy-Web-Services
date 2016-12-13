package au.gov.ga.geodesy.domain.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import au.gov.ga.geodesy.support.spring.IntegrationTest;

public class ContactTypeRepositoryITest extends IntegrationTest {

    @Autowired
    private ContactTypeRepository contactTypes;

    private String[] codes = {
        ContactType.SITE_OWNER,
        ContactType.SITE_CONTACT,
        ContactType.SITE_METADATA_CUSTODIAN,
        ContactType.SITE_DATA_CENTER,
        ContactType.SITE_DATA_SOURCE,
    };

    @Test
    public void listAll() {
        // TODO: order shouldn't matter
        assertThat(contactTypes.findAll().stream().map(ContactType::getCode).toArray(), is(codes));
    }

    @Test
    public void testSiteContact() {
        assertThat(contactTypes.siteContact().getCode(), is("SiteContact"));
    }
}
