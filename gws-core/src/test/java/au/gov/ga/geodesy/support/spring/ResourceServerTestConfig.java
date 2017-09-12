package au.gov.ga.geodesy.support.spring;

import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerTestConfig extends ResourceServerConfig {

    public final static byte[] tokenSigningKey = Base64.getEncoder().encode("shared secret".getBytes());
    public final static MacSigner macSigner = new MacSigner(new SecretKeySpec(tokenSigningKey, "HS256"));

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(new JwtTokenStore(new JwtAccessTokenConverter() {
            {
                this.setVerifier(macSigner);
            }
        }));
    }
}
