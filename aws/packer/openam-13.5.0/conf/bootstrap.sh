#!/usr/bin/env bash

sudo yum erase -y java-1.7.0*
sudo yum update -y
sudo yum install -y java-1.8.0-openjdk-devel tomcat8 perl-Switch perl-DateTime perl-Sys-Syslog \
     perl-LWP-Protocol-https ruby wget postgresql-libs

# Set up iptables restricted access and redirection of port 443 to port 8443
sudo service iptables start
sudo chkconfig --level 345 iptables on
sudo iptables -I INPUT -p tcp --dport 443 -j ACCEPT
sudo iptables -t nat -A PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 8443
sudo iptables -t nat -A OUTPUT -o lo -p tcp --dport 443 -j REDIRECT --to-port 8443
sudo service iptables save

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

# Download OpenAM from S3
sudo aws --no-sign-request s3 cp s3://geodesy-web-services/OpenAM/OpenAM-13.5.0.zip /tmp/.
sudo unzip /tmp/OpenAM-13.5.0.zip -d /tmp


# Extract OpenAM WAR to Tomcat webapp directory
sudo cp /tmp/openam/OpenAM-13.5.0.war /usr/share/tomcat8/webapps/openam.war
sudo unzip /usr/share/tomcat8/webapps/openam.war -d /usr/share/tomcat8/webapps/openam

sudo cp /tmp/server.xml /usr/share/tomcat8/conf/
sudo cp /tmp/web.xml /usr/share/tomcat8/webapps/openam/WEB-INF/

function setupTomcatSelfSignedCertificate {
    hostname=$1
    tomcatHome=/usr/share/tomcat8
    tomcatKeystore=${tomcatHome}/conf/keystore.jks
    javaKeystore=/etc/alternatives/jre_openjdk/lib/security/cacerts
    alias=tomcat
    certificate=/usr/share/tomcat8/conf/${alias}.crt

    sudo keytool -noprompt -genkey \
        -alias "${alias}" \
        -keystore "${tomcatKeystore}" \
        -dname "CN=${hostname}, OU=[unknown], O=[unknown], L=[unknown], S=[unknown], C=[unknown]" \
        -storepass changeit -keypass changeit

    sudo keytool -noprompt -exportcert \
        -alias "${alias}" \
        -keystore "${tomcatKeystore}" \
        -rfc -file "${certificate}" \
        -storepass changeit

    sudo keytool -noprompt -importcert \
        -alias "${alias}" \
        -keystore "${javaKeystore}" \
        -file "${certificate}" \
        -storepass changeit
}

setupTomcatSelfSignedCertificate "*.geodesy.ga.gov.au"

# Set the Tomcat JVM memory required by OpenAM
echo 'JAVA_OPTS="$JAVA_OPTS -Xmx1g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"' | sudo tee --append /usr/share/tomcat8/conf/tomcat8.conf

# Set Permissions
cd /usr/share
sudo chown -hR tomcat:tomcat tomcat8/
sudo chown -hR tomcat:tomcat tomcat8/**/*
sudo chmod -R 777 tomcat8/logs

# Install the openam configurator tools
sudo mkdir -p /opt/openam/tools/config
cd /opt/openam/tools/config
sudo unzip /tmp/openam/SSOConfiguratorTools-13.5.0.zip

# Install the openam admin tools
sudo mkdir -p /opt/openam/tools/admin
cd /opt/openam/tools/admin
sudo unzip /tmp/openam/SSOAdminTools-13.5.0.zip

# Cleanup
sudo rm -rf /tmp/openam
sudo rm -rf /tmp/OpenAM-13.5.0.zip

sudo service tomcat8 start
sudo chkconfig --level 345 tomcat8 on
