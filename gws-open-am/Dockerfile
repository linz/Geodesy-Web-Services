FROM gws/tomcat

RUN nix-env -iA nixpkgs.gawk

ADD https://s3-ap-southeast-2.amazonaws.com/geodesy-web-services/OpenAM/OpenAM-13.5.0.zip /tmp/
RUN (cd /tmp/ && unzip OpenAM-13.5.0.zip)
RUN (cd /tmp/ && mv openam/OpenAM-13.5.0.war openam.war)
RUN mkdir /webapps && mv /tmp/openam.war /webapps/

# Install the openam configurator tools
RUN mkdir -p /opt/openam/tools/config
RUN (cd /opt/openam/tools/config && unzip /tmp/openam/SSOConfiguratorTools-13.5.0.zip)

ADD ./env-openam.sh /

# Install the openam admin tools
RUN mkdir -p /opt/openam/tools/admin
RUN (cd /opt/openam/tools/admin && unzip /tmp/openam/SSOAdminTools-13.5.0.zip)

ADD ./configuration /tmp/openam/configuration

ADD ./export-configuration.sh /tmp/openam/
ADD ./import-configuration.sh /tmp/openam/

RUN /tmp/openam/import-configuration.sh

CMD ["catalina.sh", "run"]
