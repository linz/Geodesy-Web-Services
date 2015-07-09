package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.MockEventPublisher;

@Configuration
public class GeodesyServiceUnitTestConfig extends GeodesyServiceConfig {

    @Bean
    @Override
    public EventPublisher eventPublisher() {
        return new MockEventPublisher();
    }
}
