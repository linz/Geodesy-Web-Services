package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Configuration;

import au.gov.ga.geodesy.domain.model.AsynchronousEventPublisher;
import au.gov.ga.geodesy.domain.model.EventPublisher;

@Configuration
public class GeodesyConfig extends GeodesyCommonConfig {

    public EventPublisher concreteEventPublisher() {
        return new AsynchronousEventPublisher();
    }
}
