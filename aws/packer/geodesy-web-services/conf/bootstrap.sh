#!/usr/bin/env bash

set -e

sudo yum erase -y java-1.7.0*
sudo yum update -y

sudo yum install -y \
    wget \
    git \
    java-1.8.0-openjdk-devel \
    tomcat8 \
    postgresql95 \
    ruby \
    perl-Switch \
    perl-DateTime \
    perl-Sys-Syslog \
    perl-LWP-Protocol-https

curl -so /tmp/apache-maven-3.5.0-bin.tar.gz http://apache.mirror.digitalpacific.com.au/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.tar.gz
sudo tar -xzvf /tmp/apache-maven-3.5.0-bin.tar.gz -C /opt
sudo ln -s /opt/apache-maven-3.5.0 /opt/maven

# cloudwatch monitoring scripts
curl -so /tmp/CloudWatchMonitoringScripts-1.2.1.zip http://aws-cloudwatch.s3.amazonaws.com/downloads/CloudWatchMonitoringScripts-1.2.1.zip
sudo unzip -o -d /opt /tmp/CloudWatchMonitoringScripts-1.2.1.zip
echo '*/5 * * * * root /opt/aws-scripts-mon/mon-put-instance-data.pl --mem-util --mem-used --mem-avail --disk-space-util --disk-path=/ --from-cron' | sudo tee /etc/cron.d/cloudwatch

# cloudwatch logs agent and forwarding config
curl https://s3.amazonaws.com/aws-cloudwatch/downloads/latest/awslogs-agent-setup.py -O
sudo chmod +x ./awslogs-agent-setup.py
sudo ./awslogs-agent-setup.py -n -r ap-southeast-2 -c s3://ga-sentinel-staging/awslogs.cfg

# Install CodeDeploy Agent
cd /home/ec2-user
wget https://aws-codedeploy-ap-southeast-2.s3.amazonaws.com/latest/install
sudo chmod +x ./install
sudo ./install auto

# Install credstash
sudo pip install credstash==1.12.0

# Set Permissions
cd /usr/share
sudo chown -hR tomcat:tomcat tomcat8/
sudo chown -hR tomcat:tomcat tomcat8/**/*
sudo chmod -R 777 tomcat8/logs

sudo service tomcat8 start
sudo chkconfig --level 345 tomcat8 on
