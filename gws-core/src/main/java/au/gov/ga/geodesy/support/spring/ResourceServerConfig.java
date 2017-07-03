package au.gov.ga.geodesy.support.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ResourceServerConfig.class);

    protected SignatureVerifier getSignatureVerifier() {
        // TODO: Move public key into environment-specific configuration files.
        return new RsaVerifier(
            "-----BEGIN PUBLIC KEY-----" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo1uXz14/oHyRkBM1I97f" +
            "45nd6wvHfWGNf51qQe0/BcIBoIqokXCPAp3HJG492xUb7wNuTc8aGTbpomtIT3cS" +
            "nF6qZCrE08x4P43T/ymOS5VhbA8hD+2zaJOJY5WfvDOUTgBJ++6mo5HmmO5gxY2j" +
            "+l3gvtA2NQIqgC30fXUczKI/7quij8RiBfgAfs4CF9QydxiyilaGRb/N73PzBNdz" +
            "wEASAOa3zMUJ7PUK2okH8yVIFQYOCTygdgOwcqbHTltoFo4FDwtEY/lJQtJtG27f" +
            "h4sWii0EifQW9RlKfBOJKltocdGBasw5WbTuhItYG47eH9uPfzMG66qpWwArxi7K" +
            "gQIDAQAB" +
            "-----END PUBLIC KEY-----"
        );
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/corsNetworks").hasAuthority("superuser")
            .antMatchers(HttpMethod.PUT, "/corsNetworks/**").hasAuthority("superuser")
            .antMatchers(HttpMethod.PATCH, "/corsNetworks/**").hasAuthority("superuser")
            .antMatchers(HttpMethod.DELETE, "/corsNetworks/**").hasAuthority("superuser")

            .antMatchers(HttpMethod.PATCH, "/corsSites/**").hasAuthority("superuser")

            .antMatchers(HttpMethod.POST, "/newCorsSiteRequests").authenticated()
            .antMatchers(HttpMethod.POST, "/siteLogs/upload").authenticated()

            .antMatchers(HttpMethod.POST, "/siteLogs/validate").permitAll()
            .antMatchers(HttpMethod.POST, "/userRegistrations").permitAll()
            .antMatchers(HttpMethod.GET, "/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            .antMatchers(HttpMethod.POST, "/**").denyAll()
            .antMatchers(HttpMethod.PUT, "/**").denyAll()
            .antMatchers(HttpMethod.PATCH, "/**").denyAll()
            .antMatchers(HttpMethod.DELETE, "/**").denyAll()

            .anyRequest().denyAll()
        ;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(null);
        resources.tokenStore(new JwtTokenStore(tokenConverter()));
        resources.eventPublisher(new AuthenticationEventPublisher() {

            @Override
            public void publishAuthenticationFailure(AuthenticationException e, Authentication a) {
                log.info("Authentication failure", e);
            }

            @Override
            public void publishAuthenticationSuccess(Authentication a) {
            }
        });
    }

    @Bean
    public JwtAccessTokenConverter tokenConverter() throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifier(getSignatureVerifier());
        return converter;
    }
}
