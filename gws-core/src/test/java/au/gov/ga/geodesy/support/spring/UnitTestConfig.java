package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

import au.gov.ga.geodesy.domain.model.ContactTypeRepository;

@Configuration
@Import({
    GeodesySupportConfig.class,
})
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UnitTestConfig {

    @Bean
    public ContactTypeRepository getContactTypeRepository() {
        return null;
    }
}
