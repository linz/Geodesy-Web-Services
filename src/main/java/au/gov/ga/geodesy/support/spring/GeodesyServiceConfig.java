package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;

@Configuration
@ComponentScan(basePackages = {"au.gov.ga.geodesy.domain"})
@EnableSpringConfigured
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public abstract class GeodesyServiceConfig {

    @Bean
    public IgsSiteLogXmlMarshaller siteLogMarshaller() throws Exception {
        return new IgsSiteLogMoxyMarshaller();
    }

    @Bean
    public EventPublisher eventPublisher() {
        EventPublisher eventPublisher = concreteEventPublisher();
        return eventPublisher;
    }

    protected abstract EventPublisher concreteEventPublisher();
}
