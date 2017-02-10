package au.gov.ga.geodesy.support.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            "-----BEGIN PUBLIC KEY-----"+
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKDE67O/PN0Uj2d3JDP/MNwff3"+
            "5hPqkspsVilOwiY/yeUrvu5ghj5c8wk+ufToHx1XqmPDVUce2+FQFlnn2dnw3Nfz"+
            "Qq3xU8pGdTH/LA6WnVbha9LPTPm94Wt5lzfIv5OPRAk1AmuW5y4a7VBNd1EhLKru"+
            "Mxvqc0fH6Fnl1GJwdwIDAQAB"+
            "-----END PUBLIC KEY-----"
        );
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
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
