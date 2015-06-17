package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import au.gov.ga.geodesy.igssitelog.interfaces.xml.IgsSiteLogXmlMarshaller;
import au.gov.ga.geodesy.igssitelog.support.marshalling.moxy.IgsSiteLogMoxyMarshaller;

@Configuration
@ComponentScan(basePackages = {"au.gov.ga.geodesy"})
@EnableWebMvc
public class GeodesyConfig {

    @Bean
    public IgsSiteLogXmlMarshaller siteLogMarshaller() throws Exception {
        return new IgsSiteLogMoxyMarshaller();
    }
}
