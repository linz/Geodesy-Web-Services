package au.gov.ga.geodesy.support.spring;

import java.io.FileNotFoundException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import au.gov.ga.geodesy.domain.model.SynchronousEventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.port.NotificationPort;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLValidator;
import au.gov.ga.geodesy.port.adapter.mock.InMemoryNotificationAdapter;

@Configuration
public class GeodesyServiceTestConfig extends GeodesyServiceConfig {

    @Bean
    @Override
    public EventPublisher eventPublisher() {
        return new SynchronousEventPublisher();
    }

    @Bean
    public GeodesyMLValidator getGeodesyMLValidator() throws FileNotFoundException {
        String catalog = ResourceUtils.getFile("file:target/generated-resources/xsd/geodesyml-0.1.0/catalog.xml").getAbsolutePath();
        return new GeodesyMLValidator(catalog);
    }

    @Bean
    public NotificationPort notificationPort() {
        return new InMemoryNotificationAdapter();
    }
}
