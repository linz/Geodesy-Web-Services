from amazonia.classes.sns import SNS

from troposphere import Ref, Join, cloudwatch
from troposphere.sns import Topic, Subscription

def user_registration_topic(emails):
    return topic("UserRegistrationReceived", emails)

def new_cors_site_request_received_topic(emails):
    return topic("NewCorsSiteRequestReceived", emails)

def topic(topic_title, emails):
    topic = Topic(topic_title,
            DisplayName=Join("", [Ref("AWS::StackName"), "-", topic_title]))

    topic.Subscription = []
    for index, email in enumerate(emails):
        topic.Subscription.append(Subscription(
            topic_title + "Subscription" + str(index),
            Endpoint=email,
            Protocol="email"))
    
    return topic

def customise_stack_template(template):
    template.add_resource(user_registration_topic([]))
    template.add_resource(new_cors_site_request_received_topic([]))
    return template
