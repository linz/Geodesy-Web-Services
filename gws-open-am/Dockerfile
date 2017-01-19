FROM gws/tomcat

ADD https://s3-ap-southeast-2.amazonaws.com/geodesy-web-services/OpenAM/OpenAM-13.5.0.zip /tmp/
RUN (cd /tmp/ && unzip OpenAM-13.5.0.zip)
RUN (cd /tmp/ && mv openam/OpenAM-13.5.0.war ROOT.war)
RUN (cd /tmp && mkdir ROOT && unzip ROOT.war -d ROOT)

RUN mkdir /webapps && mv /tmp/ROOT /webapps/ROOT

CMD ["catalina.sh", "run"]
