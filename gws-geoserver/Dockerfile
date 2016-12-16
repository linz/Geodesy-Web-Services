FROM gws/tomcat

ADD https://s3-ap-southeast-2.amazonaws.com/geodesy-web-services/geoserver/geoserver-2.9.1-war.zip /tmp/
RUN (cd /tmp/ && unzip geoserver-2.9.1-war geoserver.war)
RUN (cd /tmp/ && mv geoserver.war ROOT.war)
RUN (cd /tmp && mkdir ROOT && unzip ROOT.war -d ROOT)

ADD https://s3-ap-southeast-2.amazonaws.com/geodesy-web-services/geoserver/geoserver-2.9.1-app-schema-plugin.zip /tmp
RUN unzip -o /tmp/geoserver-2.9.1-app-schema-plugin.zip *.jar -d /tmp/ROOT/WEB-INF/lib/

ADD https://s3-ap-southeast-2.amazonaws.com/geodesy-web-services/geoserver/geoserver-web.xml /tmp/ROOT/WEB-INF/web.xml

RUN rm -r /tmp/ROOT/data
ADD data /tmp/ROOT/data

ADD context.xml /tmp/ROOT/META-INF/
RUN sed -i 's,${geodesy-db-url},jdbc:postgresql://db/geodesydb,' /tmp/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-username}/geodesy/' /tmp/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-password}/geodesypw/' /tmp/ROOT/META-INF/context.xml

RUN mkdir /webapps && mv /tmp/ROOT /webapps/ROOT

CMD ["catalina.sh", "run"]
