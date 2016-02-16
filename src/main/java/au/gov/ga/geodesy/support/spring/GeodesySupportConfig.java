package au.gov.ga.geodesy.support.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"au.gov.ga.geodesy.support.moxy"})
public class GeodesySupportConfig {
}
