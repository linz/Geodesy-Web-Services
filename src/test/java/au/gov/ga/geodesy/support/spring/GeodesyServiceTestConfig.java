package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import au.gov.ga.geodesy.domain.model.SynchronousEventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;

@Configuration
public class GeodesyServiceTestConfig extends GeodesyServiceConfig {

    @Bean
    @Override
    public EventPublisher eventPublisher() {
        return new SynchronousEventPublisher();
    }
}
