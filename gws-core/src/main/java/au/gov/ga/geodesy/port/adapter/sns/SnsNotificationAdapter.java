package au.gov.ga.geodesy.port.adapter.sns;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.hateoas.config.EnableEntityLinks;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;

import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.port.Notification;
import au.gov.ga.geodesy.port.NotificationPort;
import au.gov.ga.geodesy.support.aws.Aws;

@Configurable
@EnableEntityLinks
public class SnsNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(SnsNotificationAdapter.class);

    @Autowired
    private Aws aws;

    private AmazonSNS sns;

    public SnsNotificationAdapter() {
        sns = AmazonSNSClientBuilder.standard()
            .withRegion(Regions.AP_SOUTHEAST_2)
            .withCredentials(new AWSCredentialsProviderChain(
                    new InstanceProfileCredentialsProvider(false),
                    new ProfileCredentialsProvider("geodesy"),
                    new EnvironmentVariableCredentialsProvider()))
            .build();
    }

    private Stream<String> snsTopics(Notification notification) {
        // An SNS topic ARN looks like this: arn:aws:sns:ap-southeast-2:094928090547:DevGeodesy-UserRegistrationReceived-K3F2UQVHG58F
        return sns.listTopics().getTopics().stream()
            .filter(topic -> {
                String[] tmp = topic.getTopicArn().split(":")[5].split("-"); // { "DevGeodesy", "UserRegistrationReceived", "K3F2UQVHG58F }
                String stackName = tmp[0];
                String eventName = tmp[1];

                return aws.getStackName().map(s -> s.equals(stackName)).orElse(false)
                    && notification.getSubject().equals(eventName);
            })
            .map(Topic::getTopicArn);
    }

    @Override
    public void sendNotification(Notification notification) {
        snsTopics(notification).forEach(arn -> {
            String subject = notification.getSubject();
            sns.publish(arn, notification.getBody(), subject);
            log.info("Published event " + subject + " to SNS topic " + arn);
        });
    }

    @Override
    public void sendNotification(Event event) {
        sendNotification(notification(event));
    }

    private Notification notification(Event event) {
        return new Notification() {
            public String getSubject() {
                return event.getName();
            }

            public String getBody() {
                return event.toString();
            }
        };
    }
}
