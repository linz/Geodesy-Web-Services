package au.gov.ga.geodesy.support.spring;
import org.testng.annotations.Test;
// import org.junit.Test;
// import org.easymock.classextension.EasyMock;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.gov.ga.geodesy.support.spring.ResourceServerConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.jwt.JwtHelper;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;



public class JwtTokenConverterTest {

    private ResourceServerConfig.JwytConverter converter = new ResourceServerConfig.JwytConverter();

    public static String getUser(Authentication auth) {
        try {
            if (auth != null) {
                StringBuilder userTitle= new StringBuilder();
                Object authDetails = auth.getDetails();

                if (auth.getPrincipal() != null)
                    userTitle.append((String)auth.getPrincipal());

                if (authDetails != null && authDetails instanceof OAuth2AuthenticationDetails) {
                    String jwt = ((OAuth2AuthenticationDetails) authDetails).getTokenValue();
                    String claims = JwtHelper.decode(jwt).getClaims();
                    userTitle.append("[");
                    userTitle.append((String) new ObjectMapper().readValue(claims, HashMap.class).get("sub"));
                    userTitle.append("]");
                }

                if (userTitle.toString().length() > 0)
                    return userTitle.toString();
            } 
        } catch (Exception e) {
            System.out.println("Failed to extract username from spring security context");
        }
        return null;
    }

    @Test
    public void shouldHasPrincipalWhenUsernameNotBlank() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ResourceServerConfig.JwytConverter.USERNAME_FD, "jefferyLiang");
        Authentication authentication = converter.extractAuthentication(map);

        // HashMap<String, Object> info = new HashMap<String, Object>();
        // info.put("sub", "qn3343-m4432");
        // authentication.setDetails((Object)info);
        // System.out.println(authentication.getPrincipal());

        assertThat(authentication.getPrincipal(), is("jefferyLiang"));
        assertThat(JwtTokenConverterTest.getUser(authentication), is("jefferyLiang"));
        assertThat(JwtTokenConverterTest.getUser(SecurityContextHolder.getContext().getAuthentication()), is("jefferyLiang"));    
    }


    @Test
    public void shouldPrincipalNullWhenUsernameBlank() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ResourceServerConfig.JwytConverter.USERNAME_FD, "");
        Authentication authentication = converter.extractAuthentication(map);
        System.out.println(authentication.getPrincipal());
        assertThat(authentication.getPrincipal(), is(nullValue()));
    }    


    @Test
    public void shouldNoAuthorityWhenSitelogEmpty() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(UserAuthenticationConverter.USERNAME, "test_user");
        map.put(ResourceServerConfig.JwytConverter.AUHTORITY_FD, "");
        Authentication authentication = converter.extractAuthentication(map);
        System.out.println(authentication);

        assertThat(authentication.getAuthorities().size(), is(0));
        assertThat(authentication.getPrincipal(), is("test_user"));
        // assertThat("User authority", 
        //     AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
        //     hasItems(new String[]{"edit-network:geonet", "edit-wgtn", "edit-auck"}));
    }

    @Test
    public void shouldExtractAuthenticationWhenAuthoritiesIsCollection() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sub", "q1x8ecTa4-bec84mf");
        map.put(ResourceServerConfig.JwytConverter.USERNAME_FD, "test_user");
        map.put(ResourceServerConfig.JwytConverter.AUHTORITY_FD, "network:geonet,WGTN,AUCK");

        Authentication authentication = converter.extractAuthentication(map);

        assertThat(authentication.getAuthorities().size(), is(3));
        assertThat("User authority", 
            AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
            hasItems(new String[]{"edit-network:geonet", "edit-wgtn", "edit-auck"}));
    }

    @Test
    public void shouldBeSuperuserWhenAuthoritiesIsSuperUser() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(UserAuthenticationConverter.USERNAME, "test_user");
        map.put(ResourceServerConfig.JwytConverter.AUHTORITY_FD, "superuser");

        Authentication authentication = converter.extractAuthentication(map);
        System.out.println(authentication.getAuthorities());
        assertThat(authentication.getAuthorities().size(), is(1));
        assertThat("Superser authority", 
            AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
            hasItems(new String[]{"superuser"}));
    }

    @Test
    public void shouldBeSuperuserWhenAuthoritiesContainsSuperUser() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(UserAuthenticationConverter.USERNAME, "test_user");
        map.put(ResourceServerConfig.JwytConverter.AUHTORITY_FD, "superuser,WGTN,network:geonet");

        Authentication authentication = converter.extractAuthentication(map);
        System.out.println(authentication.getAuthorities());
        assertThat(authentication.getAuthorities().size(), is(1));
        assertThat("Superser authority", 
            AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
            hasItems(new String[]{"superuser"}));
    }

    @Test
    public void shouldWorkWhenAuthoritiesIsLongString() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(UserAuthenticationConverter.USERNAME, "test_user");
        map.put(ResourceServerConfig.JwytConverter.AUHTORITY_FD, "WGTN,network:geonet,network:geonet2,network:geonet3,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT");

        Authentication authentication = converter.extractAuthentication(map);
        assertThat(authentication.getAuthorities().size(), is(36));

        assertThat("User authority from network", 
            AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
            hasItems(new String[]{"edit-network:geonet", "edit-network:geonet2", "edit-network:geonet3"})); 

        assertThat("User authority from site", 
            AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
            hasItems(new String[]{"edit-alit", "edit-auck", "edit-ghnp"}));                   
    }

    @Test
    public void shouldWorkWithCorrectCaseWhenAuthoritiesIsLongString() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(UserAuthenticationConverter.USERNAME, "test_user");
        map.put(ResourceServerConfig.JwytConverter.AUHTORITY_FD, "WGTN,network:geonet,network:GEONET2,network:geonet3,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT,GHNP,AUCK,ALIT,LICT");

        Authentication authentication = converter.extractAuthentication(map);
        assertThat(authentication.getAuthorities().size(), is(36));

        assertThat("User authority from network", 
            AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
            not(hasItems(new String[]{"edit-network:GEONET2", "edit-network:GEONET3"}))); 

        assertThat("User authority from site", 
            AuthorityUtils.authorityListToSet(authentication.getAuthorities()), 
            not(hasItems(new String[]{"edit-ALIT", "edit-AUCK", "edit-GHNP"})));                   
    }    
}


