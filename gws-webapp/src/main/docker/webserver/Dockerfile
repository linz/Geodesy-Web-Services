FROM gws/tomcat

ADD target/geodesy-web-services.war /webapps/ROOT.war

RUN (cd /webapps && mkdir ROOT && unzip ROOT.war -d ROOT)
RUN sed -i 's,${geodesy-db-url},jdbc:postgresql://db/geodesydb,' /webapps/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-username}/geodesy/' /webapps/ROOT/META-INF/context.xml
RUN sed -i 's/${geodesy-db-password}/geodesypw/' /webapps/ROOT/META-INF/context.xml

# RUN sed -i 's,${oauthProviderUrl},https://devgeodesy-openam.geodesy.ga.gov.au/openam/oauth2,' /webapps/ROOT/WEB-INF/classes/config.properties
RUN sed -i 's,${oauthProviderUrl},https://cognito-idp.ap-southeast-2.amazonaws.com/ap-southeast-2_q0oHPJ0N5,' /webapps/ROOT/WEB-INF/classes/config.properties

CMD ["catalina.sh", "run"]
