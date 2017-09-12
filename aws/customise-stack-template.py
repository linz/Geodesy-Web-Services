from amazonia.classes.sns import SNS

from troposphere import Ref, Join, cloudwatch
from troposphere.sns import Topic, TopicPolicy, Subscription

def user_registration_topic(emails):
    return topic("UserRegistrationReceived", emails)

def new_cors_site_request_received_topic(emails):
    return topic("NewCorsSiteRequestReceived", emails)

def site_log_received_topic(emails):
    return topic("SiteLogReceived", emails)

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

def site_log_received_topic_policy_document(topic, subscriber_principal):
    return { "Version": "2008-10-17",
             "Id": "__default_policy_ID",
             "Statement": [
               {
                 "Sid": "__default_statement_ID",
                 "Effect": "Allow",
                 "Principal": {
                   "AWS": "*"
                 },
                 "Action": [
                   "SNS:Publish",
                   "SNS:RemovePermission",
                   "SNS:SetTopicAttributes",
                   "SNS:DeleteTopic",
                   "SNS:ListSubscriptionsByTopic",
                   "SNS:GetTopicAttributes",
                   "SNS:Receive",
                   "SNS:AddPermission",
                   "SNS:Subscribe"
                 ],
                 "Resource": Ref(topic),
                 "Condition": {
                   "StringEquals": {
                     "AWS:SourceOwner": Ref("AWS::AccountId")
                   }
                 }
               },
               {
                 "Sid": "lambda-access",
                 "Effect": "Allow",
                 "Principal": {
                   "AWS": subscriber_principal
                 },
                 "Action": [
                   "SNS:Subscribe",
                   "SNS:ListSubscriptionsByTopic",
                   "SNS:Receive"
                 ],
                 "Resource": Ref(topic)
               }
             ]
           }

def customise_stack_template(template, env_variables):
    template.add_resource(user_registration_topic([]))
    template.add_resource(new_cors_site_request_received_topic([]))

    topic = site_log_received_topic([])
    template.add_resource(topic)
    template.add_resource(
            TopicPolicy("SiteLogReceivedTopicPolicy",
                PolicyDocument=site_log_received_topic_policy_document(
                    topic,
                    env_variables['site_log_received_topic_subscriber_principal']),
                Topics=[Ref(topic)]))

    return template
