package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.EventRepositoryPublisher;

@Configuration
public class GeodesyConfig extends GeodesyCommonConfig {

    @Bean
    public EventPublisher eventPublisher() {
        return new EventRepositoryPublisher();
    }
}
