#!/usr/bin/env bash

sudo yum erase -y java-1.7.0*
sudo yum update -y
sudo yum install -y java-1.8.0-openjdk-devel tomcat8 perl-Switch perl-DateTime perl-Sys-Syslog \
     perl-LWP-Protocol-https ruby wget postgresql-libs

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

# Install postgresql client
sudo yum install -y postgresql

# Install credstash
sudo pip install credstash

# Download 52north from S3
# wget http://52north.org/downloads/send/3-sos/490-52n-sensorweb-sos-4-3-7 -O 52n-sensorweb-sos-4-3-7.zip
sudo aws --no-sign-request s3 cp s3://geodesy-web-services/52north/52n-sensorweb-sos-4.3.7.zip .

# Extract 52north WAR to Tomcat webapp directory
sudo unzip -j 52n-sensorweb-sos-4.3.7.zip 52n-sensorweb-sos-4.3.7/bin/target/52n-sos-webapp##4.3.7.war -d /usr/share/tomcat8/webapps/.
sudo unzip /usr/share/tomcat8/webapps/52n-sos-webapp##4.3.7.war -d /usr/share/tomcat8/webapps/52n-sos-webapp##4.3.7

# Clean up
sudo rm 52n-sensorweb-sos-4.3.7.zip

# Set Permissions
cd /usr/share
sudo chown -hR tomcat:tomcat tomcat8/
sudo chown -hR tomcat:tomcat tomcat8/**/*
sudo chmod -R 777 tomcat8/logs

