package au.gov.ga.geodesy.port.adapter.smtp;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.InvalidSiteLogReceived;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.domain.model.event.SiteUpdated;
import au.gov.ga.geodesy.domain.model.event.WeeklySolutionAvailable;
import au.gov.ga.geodesy.domain.service.NotificationService;
import au.gov.ga.geodesy.support.email.SpringMailAdapter;
import au.gov.ga.geodesy.support.properties.GeodesyMailConfig;
import au.gov.ga.geodesy.support.properties.GeodesyNotificationsConfig;
import au.gov.ga.geodesy.support.spring.TestAppConfig;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

@ContextConfiguration(
    classes = {TestAppConfig.class,
        SpringMailAdapter.class,
        SMTPNotificationAdapter.class,
        NotificationService.class,
        GeodesyMailConfig.class,
        GeodesyNotificationsConfig.class},
    loader = AnnotationConfigContextLoader.class)
public class SMTPNotificationAdapterTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private SMTPNotificationAdapter smtpNotifier;

    @Autowired
    private JavaMailSenderImpl emailSender;

    @Autowired
    private GeodesyNotificationsConfig geodesyNotificationsConfig;

    private GreenMail testSMTP;

    @BeforeMethod
    public void begin() {
        testSMTP = new GreenMail(ServerSetupTest.SMTP);
        testSMTP.start();

        emailSender.setPort(3025);
        emailSender.setHost("localhost");
    }

    @AfterMethod
    public void end() throws FolderException {
        testSMTP.stop();
    }

    private void assertMessageContentsAsExpected(String content, Event event, String fromConfig, Message[] messages) throws
        MessagingException, IOException {
        for (Message message : messages) {
            Address from = message.getFrom()[0];
            Assert.assertEquals(fromConfig, from.toString());
            MatcherAssert.assertThat("the message content", (String) message.getContent(), Matchers.containsString(event.getMessage()));
            MatcherAssert.assertThat("the message content", (String) message.getContent(), Matchers.containsString(content));

            System.out.println("Message: " + message.getContent());
        }
    }

    @Test
    public void testNotificationServiceInvalidSiteLogReceived() throws MessagingException, IOException {
        String content = "Sitelog text";
        InvalidSiteLogReceived event = new InvalidSiteLogReceived(content);
        int numberMsgEqualNumberTos = geodesyNotificationsConfig.getInvalidSiteLogReceivedEmailAddressees().size();
        String fromConfig = geodesyNotificationsConfig.getFromEmail();
        smtpNotifier.sendNotification(event);

        // Check emails

        Message[] messages = testSMTP.getReceivedMessages();
        Assert.assertEquals(numberMsgEqualNumberTos, messages.length);
        assertMessageContentsAsExpected(content, event, fromConfig, messages);
    }

    @Test
    public void testNotificationServiceSiteLogReceived() throws MessagingException, IOException {
        String fourCharID = "ALIC";
        SiteLogReceived event = new SiteLogReceived(fourCharID);
        int numberMsgEqualNumberTos = geodesyNotificationsConfig.getInvalidSiteLogReceivedEmailAddressees().size();
        String fromConfig = geodesyNotificationsConfig.getFromEmail();
        smtpNotifier.sendNotification(event);

        // Check emails

        Message[] messages = testSMTP.getReceivedMessages();
        Assert.assertEquals(numberMsgEqualNumberTos, messages.length);
        assertMessageContentsAsExpected(fourCharID, event, fromConfig, messages);
    }

    @Test
    public void testNotificationServiceSiteUpdated() throws MessagingException, IOException {
        String fourCharID = "ALIC";
        SiteUpdated event = new SiteUpdated(fourCharID);
        int numberMsgEqualNumberTos = geodesyNotificationsConfig.getInvalidSiteLogReceivedEmailAddressees().size();
        String fromConfig = geodesyNotificationsConfig.getFromEmail();
        smtpNotifier.sendNotification(event);

        // Check emails

        Message[] messages = testSMTP.getReceivedMessages();
        Assert.assertEquals(numberMsgEqualNumberTos, messages.length);
        assertMessageContentsAsExpected(fourCharID, event, fromConfig, messages);
    }

    @Test
    public void testNotificationServiceWeeklySolutionAvailable() throws MessagingException, IOException {
        Integer weeklySolutionId = 123456789;
        WeeklySolutionAvailable event = new WeeklySolutionAvailable(weeklySolutionId);
        int numberMsgEqualNumberTos = geodesyNotificationsConfig.getInvalidSiteLogReceivedEmailAddressees().size();
        String fromConfig = geodesyNotificationsConfig.getFromEmail();
        smtpNotifier.sendNotification(event);

        // Check emails

        Message[] messages = testSMTP.getReceivedMessages();
        Assert.assertEquals(numberMsgEqualNumberTos, messages.length);
        assertMessageContentsAsExpected(weeklySolutionId.toString(), event, fromConfig, messages);
    }

}
