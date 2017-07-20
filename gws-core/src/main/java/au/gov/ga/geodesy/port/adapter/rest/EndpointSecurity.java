package au.gov.ga.geodesy.port.adapter.rest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.CorsNetwork;
import au.gov.ga.geodesy.domain.model.CorsNetworkRepository;
import au.gov.ga.geodesy.domain.model.CorsSite;
import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.NetworkTenancy;

@Component
public class EndpointSecurity {

    @Autowired
    private CorsSiteRepository sites;

    @Autowired
    private CorsNetworkRepository networks;

    /**
     * Return true if the currently authenticated user has the given authority, false otherwise.
     */
    public boolean hasAuthority(String authority) {
        return hasAnyAuthority(Collections.singletonList(authority));
    }

    /**
     * Return true if the currently authenticated user has any of the given authorities, false otherwise.
     */
    public boolean hasAnyAuthority(List<String> authorities) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
            .stream()
            .anyMatch(grantedAuthority -> authorities.contains(grantedAuthority.getAuthority()));
    }

    /**
     * Return true if the currently authenticated user has the authority to edit the given site log,
     * false otherwise.
     */
    public boolean hasAuthorityToEditSiteLog(String fourCharId) {
        if (hasAuthority("superuser")) {
            return true;
        }
        String siteEditAuthority = "edit-" + fourCharId.toLowerCase();
        if (hasAuthority(siteEditAuthority)) {
            return true;
        }
        CorsSite site = sites.findByFourCharacterId(fourCharId);
        if (site == null) {
            return false;
        }
        List<String> networkEditAuthorities = site.getNetworkTenancies().stream()
            .filter(NetworkTenancy::inEffect)
            .map(networkTenancy -> networks.findOne(networkTenancy.getCorsNetworkId()))
            .map(CorsNetwork::getName)
            .map(networkName -> "edit-network:" + networkName.toLowerCase())
            .collect(Collectors.toList());

        if (hasAnyAuthority(networkEditAuthorities)) {
            return true;
        }
        return false;
    }

    /**
      * Throw an exception if the currently authenticated user does not have the authority to edit the given site log.
      */
    public void assertAuthorityToEditSiteLog(String fourCharId) throws AccessDeniedException {
        if (!hasAuthorityToEditSiteLog(fourCharId)) {
            throw new AccessDeniedException("Cannot edit site " + fourCharId);
        }
    }
}
