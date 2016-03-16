package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import au.gov.ga.geodesy.domain.model.event.AsynchronousEventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;

@Configuration
@EnableSpringConfigured
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"au.gov.ga.geodesy.support.moxy","au.gov.ga.geodesy.domain.service","au.gov.ga.geodesy.domain.model.equipment"})
public class GeodesyServiceConfig {

    @Bean
    public EventPublisher eventPublisher() {
        return new AsynchronousEventPublisher();
    }
}
