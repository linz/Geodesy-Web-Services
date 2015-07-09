package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Configuration;

import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.MockEventPublisher;

@Configuration
public class GeodesyUnitTestConfig extends GeodesyServiceConfig {

    public EventPublisher concreteEventPublisher() {
        return new MockEventPublisher();
    }
}
