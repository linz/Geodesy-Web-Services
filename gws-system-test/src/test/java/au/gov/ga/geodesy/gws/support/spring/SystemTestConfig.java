package au.gov.ga.geodesy.gws.support.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Spring context configuration for system tests.
 *
 * @see {@code src/test/resources/system-test.properties}
 */
@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource("classpath:/system-test.properties")
public class SystemTestConfig {

    @Value("${webServicesUrl}")
    private String webServicesUrl;

    @Value("${http.useProxy}")
    private Boolean useProxy;

    @Value("${http.proxyHost}")
    private String httpProxyHost;

    @Value("${http.proxyPort}")
    private Short httpProxyPort;

    @Value("${http.proxyUser}")
    private String httpProxyUser;

    @Value("${http.proxyPassword}")
    private String httpProxyPassword;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String getWebServicesUrl() {
        return webServicesUrl;
    }

    public Boolean getUseProxy() {
        return useProxy;
    }

    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    public Short getHttpProxyPort() {
        return httpProxyPort;
    }

    public String getHttpProxyUser() {
        return httpProxyUser;
    }

    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }
}
