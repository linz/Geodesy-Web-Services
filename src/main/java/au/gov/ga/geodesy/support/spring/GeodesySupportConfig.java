package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.MarshallingException;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {
    "au.gov.ga.geodesy.support.mapper",
    "au.gov.ga.geodesy.support.persistence.jpa",
    "au.gov.ga.geodesy.support.moxy",
})
public class GeodesySupportConfig {
    @Bean
    public GeodesyMLMarshaller getGeodesyMLMoxy() throws MarshallingException {
        return new GeodesyMLMoxy();
    }

	@Bean
    public IgsSiteLogXmlMarshaller siteLogMarshaller() throws au.gov.ga.geodesy.igssitelog.interfaces.xml.MarshallingException {
        return new IgsSiteLogMoxyMarshaller();
    }

    /**
     * @return a PropertySourcesPlaceholderConfigurer for wiring @value ${xxx} resources
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();

        return p;
    }
}
