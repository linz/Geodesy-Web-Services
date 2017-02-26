package au.gov.ga.geodesy.support.spring;

import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
public class ResourceServerTestConfig extends ResourceServerConfig {

    // TODO: Move shared secret into environment-specific configuration files.
    public final static byte[] tokenSigningKey = Base64.getEncoder().encode("shared secret".getBytes());
    public final static MacSigner macSigner = new MacSigner(new SecretKeySpec(tokenSigningKey, "HS256"));

    @Override
    protected SignatureVerifier getSignatureVerifier() {
        return macSigner;
    }
}
