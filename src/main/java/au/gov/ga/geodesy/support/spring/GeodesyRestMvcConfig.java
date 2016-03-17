package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"au.gov.ga.geodesy.port.adapter.rest"})
public class GeodesyRestMvcConfig extends WebMvcConfigurerAdapter {
}
