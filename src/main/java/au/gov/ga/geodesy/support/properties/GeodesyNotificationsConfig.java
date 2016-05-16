package au.gov.ga.geodesy.support.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:notifications.properties")
/**
 * The notification email properties contains comma delimitered emails
 * 
 * @author brookes
 *
 */
public class GeodesyNotificationsConfig {

    @Value("${Events.InvalidSiteLogReceived.emailAddressees}")
    private String emailAddresseesInvalidSiteLogReceivedRaw;
    @Value("${Events.SiteLogReceived.emailAddressees}")
    private String emailAddresseesSiteLogReceivedRaw;
    @Value("${Events.SiteUpdated.emailAddressees}")
    private String emailAddresseesSiteUpdatedRaw;
    @Value("${Events.WeeklySolutionAvailable.emailAddressees}")
    private String emailAddresseesWeeklySolutionAvailableRaw;

    private List<String> invalidSiteLogReceivedEmailAddressees;
    private List<String> siteLogReceivedEmailAddressees;
    private List<String> siteUpdatedEmailAddressees;
    private List<String> weeklySolutionAvailableEmailAddressees;

    @Value("${Events.from.email}")
    private String emailFrom;

    public List<String> getInvalidSiteLogReceivedEmailAddressees() {
        if (invalidSiteLogReceivedEmailAddressees == null) {
            invalidSiteLogReceivedEmailAddressees = StringUtils.isBlank(emailAddresseesInvalidSiteLogReceivedRaw) ? new ArrayList<>()
                    : splitify(emailAddresseesInvalidSiteLogReceivedRaw);
        }
        return invalidSiteLogReceivedEmailAddressees;
    }

    public List<String> getSiteLogReceivedEmailAddressees() {
        if (siteLogReceivedEmailAddressees == null) {
            siteLogReceivedEmailAddressees = StringUtils.isBlank(emailAddresseesSiteLogReceivedRaw) ? new ArrayList<>()
                    : splitify(emailAddresseesSiteLogReceivedRaw);
        }
        return siteLogReceivedEmailAddressees;
    }

    public List<String> getSiteUpdatedEmailAddressees() {
        if (siteUpdatedEmailAddressees == null) {
            siteUpdatedEmailAddressees = StringUtils.isBlank(emailAddresseesSiteUpdatedRaw) ? new ArrayList<>()
                    : splitify(emailAddresseesSiteUpdatedRaw);
        }
        return siteUpdatedEmailAddressees;
    }

    public List<String> getWeeklySolutionAvailableEmailAddressees() {
        if (weeklySolutionAvailableEmailAddressees == null) {
            weeklySolutionAvailableEmailAddressees = StringUtils.isBlank(emailAddresseesWeeklySolutionAvailableRaw) ? new ArrayList<>()
                    : splitify(emailAddresseesWeeklySolutionAvailableRaw);
        }
        return weeklySolutionAvailableEmailAddressees;
    }

    private List<String> splitify(String input) {
        List<String> split = Arrays.asList(input.split("\\W*,\\W*"));
        return split;
    }
    
    public String getFromEmail() {
        return emailFrom;
    }

}
