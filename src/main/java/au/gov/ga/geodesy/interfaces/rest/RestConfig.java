package au.gov.ga.geodesy.interfaces.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;

@Configuration
public class RestConfig extends RepositoryRestMvcConfiguration {

   @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(IgsSiteLog.class);
    }
}
