package au.gov.ga.geodesy.support.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * GWS Configuration File
 */
@Configuration
@PropertySource("classpath:/config.properties")
public class Config {

    @Value("${oauthProviderUrl}")
    private String oauthProviderUrl;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String getOAuthProviderUrl() {
        return oauthProviderUrl;
    }
}
