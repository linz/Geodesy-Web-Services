package au.gov.ga.geodesy.support.spring;

import org.apache.commons.collections.map.HashedMap;

import java.lang.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
// import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mashape.unirest.http.Unirest;
// https://github.com/GeoscienceAustralia/Geodesy-Web-Services/commit/7b9638ab407914a2a06c778334057ee0143a7cd8#diff-adf5d3d830d4343a7764327f5866fd68

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ResourceServerConfig.class);

    @Autowired
    private Config config;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/corsNetworks").hasAuthority("superuser")
            .antMatchers(HttpMethod.PUT, "/corsNetworks/**").hasAuthority("superuser")
            .antMatchers(HttpMethod.PATCH, "/corsNetworks/**").hasAuthority("superuser")
            .antMatchers(HttpMethod.DELETE, "/corsNetworks/**").hasAuthority("superuser")

            .antMatchers(HttpMethod.PATCH, "/corsSites/**").hasAuthority("superuser")
            .antMatchers(HttpMethod.PUT, "/corsSites/*/addToNetwork").hasAuthority("superuser")

            .antMatchers(HttpMethod.POST, "/newCorsSiteRequests").authenticated()
            .antMatchers(HttpMethod.POST, "/siteLogs/upload").authenticated()
            .antMatchers(HttpMethod.GET, "/siteLogs/isAuthorisedToUpload").authenticated()

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

    public static class JwytConverter extends DefaultAccessTokenConverter {
        private final String field_username = "username";
        private final String field_authorities = "custom:sitelog";

        // TODO: testcase like 
        //  https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/java/org/
        //                             springframework/security/oauth2/provider/token/DefaultAccessTokenConverterTests.java#L66:24
        //
        // {"sub":"5cc400c3-5df4-4d56-a999-1de9efb60c1f","cognito:groups":["edit-network:geonet","edit-network:gpsnet","B1"],
        //     "iss":"https:\/\/cognito-idp.ap-southeast-2.amazonaws.com\/ap-southeast-2_q0oHPJ0N5",
        //         "version":2,"client_id":"4hpg5n34kduiedr7taqipdo77h","event_id":"8599e569-1847-11e8-9ea2-7bd7a125231f",
        //         "token_use":"access","scope":"aws.cognito.signin.user.admin openid profile",
        //         "auth_time":1519355610,"exp":1519359210,
        //         "iat":1519355610,"jti":"c55c5eb4-2ffd-4245-a568-01d027962cef",
        //         "username":"jtest1"}


        @SuppressWarnings("unchecked")
        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> hmap) {
            Map<String, Object> userMap = new HashMap(hmap);


            if(hmap.containsKey(field_authorities) && hmap.get(field_authorities) != null ) {
                String[] authority_roles = ((String)hmap.get(field_authorities)).split(",");
                // System.out.println("userMap | auhority_roles->" + authority_roles);

                List<String> authorities = new ArrayList<String>();
                if (Arrays.asList(authority_roles).stream().anyMatch(p -> p.equals("superuser"))) {
                    authorities = Arrays.asList("superuser");
                } else {
                    authorities  = Arrays.asList(authority_roles).stream().map(p -> "edit-" + p.toLowerCase()).collect(toList());
                }


                // List<String> authorities = Arrays.asList(authority_roles).stream().map(s -> "edit-" + s.toLowerCase()).collect(toList());
                userMap.put("authorities", authorities);
            }

            if(hmap.containsKey(field_username) && hmap.get(field_username) != null ) {
                String subject = (String)hmap.get(field_username);
                userMap.put("sub", subject);
                userMap.remove(field_username);
            }
            
            System.out.println("Changed - userMap->" + Arrays.asList(userMap));

            OAuth2Authentication auth = super.extractAuthentication(userMap);
            System.out.println( "extractAuthentication>>>>." + auth);

            return auth;
        }
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(null);

        String jwksUri = Unirest.get(config.getOAuthProviderUrl() + "/.well-known/openid-configuration")
            .asJson()
            .getBody()
            .getObject()
            .getString("jwks_uri");

//        System.out.println("SiteLogMapper =======================================================");
//        System.out.println(jwksUri + " ==================");
//        System.out.println("SiteLogMapper =======================================================");

        TokenStore mts = new JwkTokenStore(jwksUri, new JwytConverter());
        resources.tokenStore(mts);
        resources.eventPublisher(new AuthenticationEventPublisher() {

            @Override
            public void publishAuthenticationFailure(AuthenticationException e, Authentication a) {
                log.info("Authentication failure", e);
            }

            @Override
            public void publishAuthenticationSuccess(Authentication a) {
//                new Exception().printStackTrace();
//                log.warn(">>>>>> hasAnyAuthority", new Exception());
                log.info("publishAuthenticationSuccess +++ Authentication:" + a.getDetails());

            }



/*            private String getUser() {
                try {

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                    System.out.println(">>>> SecurityContextHolder.getContext().getAuthentication >> " + SecurityContextHolder.getContext().getAuthentication());

                    if (auth != null) {
                        Object authDetails = auth.getDetails();

                        if (authDetails instanceof OAuth2AuthenticationDetails) {
                            String jwt = ((OAuth2AuthenticationDetails) authDetails).getTokenValue();
                            String claims = JwtHelper.decode(jwt).getClaims();


                            System.out.println("[Sub]:" + new ObjectMapper().readValue(claims, HashMap.class).get("sub"));
                            System.out.println("[iss]:" + new ObjectMapper().readValue(claims, HashMap.class).get("iss"));
                            System.out.println("[cognito:groups]:" + new ObjectMapper().readValue(claims, HashMap.class).get("cognito:groups"));
                            System.out.println("[username]:" + new ObjectMapper().readValue(claims, HashMap.class).get("username"));

                            return (String) new ObjectMapper().readValue(claims, HashMap.class).get("iss");
                        }
                    } 
                } catch (Exception e) {
                    log.error("Failed to extract username from spring security context", e);
                }
                return null;
            }*/

        });
    }
}
