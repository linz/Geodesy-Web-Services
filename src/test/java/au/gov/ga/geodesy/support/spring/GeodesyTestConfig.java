package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Configuration;

import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.SynchronousEventPublisher;


@Configuration
public class GeodesyTestConfig extends GeodesyCommonConfig {

    public EventPublisher concreteEventPublisher() {
        return new SynchronousEventPublisher();
    }
}
