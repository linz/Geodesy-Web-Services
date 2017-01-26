FROM gws/tomcat

ADD https://s3-ap-southeast-2.amazonaws.com/geodesy-web-services/OpenAM/OpenAM-13.5.0.zip /tmp/
RUN (cd /tmp/ && unzip OpenAM-13.5.0.zip)
RUN (cd /tmp/ && mv openam/OpenAM-13.5.0.war openam.war)
RUN mkdir /webapps && mv /tmp/openam.war /webapps/

# Install the openam configurator tools
RUN mkdir -p /opt/openam/tools/config
RUN (cd /opt/openam/tools/config && unzip /tmp/openam/SSOConfiguratorTools-13.5.0.zip)

ADD ./openam.cfg /tmp/openam/

ADD ./configure-openam.sh /tmp/openam/
RUN /tmp/openam/configure-openam.sh

CMD ["catalina.sh", "run"]
