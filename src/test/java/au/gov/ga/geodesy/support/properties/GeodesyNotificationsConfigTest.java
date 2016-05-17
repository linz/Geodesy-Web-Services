package au.gov.ga.geodesy.support.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.gov.ga.geodesy.support.spring.TestAppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, GeodesyNotificationsConfig.class, GeodesyMailConfig.class})
public class GeodesyNotificationsConfigTest {

    @Autowired
    GeodesyNotificationsConfig config;

    List<String> baseEmails = Arrays.asList("brooke.smith_%@ga.gov.au", "lazar.bodor_%@ga.gov.au");

    @Test
    public void testGeodesyNotificationConfig01() {
        Assert.assertEquals(2, config.getInvalidSiteLogReceivedEmailAddressees().size());
        Assert.assertThat(config.getInvalidSiteLogReceivedEmailAddressees(),
                Matchers.hasItems(buildEmailsArray("InvalidSiteLogReceived")));

        Assert.assertEquals(2, config.getSiteLogReceivedEmailAddressees().size());
        Assert.assertThat(config.getSiteLogReceivedEmailAddressees(),
                Matchers.hasItems(buildEmailsArray("SiteLogReceived")));

        Assert.assertEquals(2, config.getSiteUpdatedEmailAddressees().size());
        Assert.assertThat(config.getSiteUpdatedEmailAddressees(), Matchers.hasItems(buildEmailsArray("SiteUpdated")));

        Assert.assertEquals(2, config.getWeeklySolutionAvailableEmailAddressees().size());
        Assert.assertThat(config.getWeeklySolutionAvailableEmailAddressees(),
                Matchers.hasItems(buildEmailsArray("WeeklySolutionAvailable")));

        Assert.assertEquals("lazar.bodor@ga.gov.au", config.getFromEmail());
    }

    private String[] buildEmailsArray(String eventName) {
        List<String> fixedEmails = new ArrayList<>();

        for (String base : baseEmails) {
            fixedEmails.add(base.replace("%", eventName));
        }

        return fixedEmails.toArray(new String[0]);
    }

}
