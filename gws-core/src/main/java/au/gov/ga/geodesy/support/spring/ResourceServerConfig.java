package au.gov.ga.geodesy.support.spring;

import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // TODO: Move shared secret into environment-specific configuration files.
    public final static byte[] tokenSigningKey = Base64.getEncoder().encode("shared secret".getBytes());

    public final static MacSigner signer = new MacSigner(new SecretKeySpec(ResourceServerConfig.tokenSigningKey, "HS256"));

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(new JwtTokenStore(tokenConverter()));
    }

    @Bean
    public JwtAccessTokenConverter tokenConverter() throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(new String(tokenSigningKey));
        return converter;
    }
}
