package au.gov.ga.geodesy.support.properties;

import au.gov.ga.geodesy.support.spring.TestAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@ContextConfiguration(
    classes = {TestAppConfig.class,
        GeodesyMailConfig.class,
        GeodesyNotificationsConfig.class},
    loader = AnnotationConfigContextLoader.class)
public class GeodesyNotificationsConfigTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GeodesyNotificationsConfig config;

    List<String> baseEmails = Arrays.asList("brooke.smith_%@ga.gov.au", "lazar.bodor_%@ga.gov.au");

    @Test
    public void testGeodesyNotificationConfig01() {
        assertThat(config.getInvalidSiteLogReceivedEmailAddressees().size(), equalTo(2));
        assertThat(config.getInvalidSiteLogReceivedEmailAddressees(),
                equalTo((buildEmailsArray("InvalidSiteLogReceived"))));

        assertThat(config.getSiteLogReceivedEmailAddressees().size(),equalTo(2));
        assertThat(config.getSiteLogReceivedEmailAddressees(),
                hasItems(buildEmailsArray("SiteLogReceived").toArray(new String[0])));

        assertThat(config.getSiteUpdatedEmailAddressees().size(),equalTo(2));
        assertThat(config.getSiteUpdatedEmailAddressees(), hasItems(buildEmailsArray("SiteUpdated").toArray(new String[0])));

        assertThat(config.getWeeklySolutionAvailableEmailAddressees().size(), equalTo(2));
        assertThat(config.getWeeklySolutionAvailableEmailAddressees(),
                hasItems(buildEmailsArray("WeeklySolutionAvailable").toArray(new String[0])));

        assertThat(config.getFromEmail(),equalTo("lazar.bodor@ga.gov.au"));
    }

    private List<String> buildEmailsArray(String eventName) {
        List<String> fixedEmails = new ArrayList<>();

        for (String base : baseEmails) {
            fixedEmails.add(base.replace("%", eventName));
        }

        return fixedEmails;//.toArray(new String[0]);
    }

}
