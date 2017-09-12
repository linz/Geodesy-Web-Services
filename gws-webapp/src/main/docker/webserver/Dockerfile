FROM gws/tomcat

ADD target/geodesy-web-services.war /webapps/ROOT.war

RUN (cd /webapps && mkdir ROOT && unzip ROOT.war -d ROOT)
RUN sed -i 's,${geodesy-db-url},jdbc:postgresql://db/geodesydb,' /webapps/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-username}/geodesy/' /webapps/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-password}/geodesypw/' /webapps/ROOT/META-INF/context.xml

RUN sed -i 's,${oauthProviderUrl},http://open-am:8080/openam/oauth2,' /webapps/ROOT/WEB-INF/classes/config.properties

CMD ["catalina.sh", "run"]
