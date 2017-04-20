package au.gov.ga.geodesy.port.adapter.rest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;

public class EndpointSecurity {

    /**
     * Return true if the currently authenticated user has the given authority, false otherwise.
     */
    public static boolean hasAuthority(String authority) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
            .stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }

    /**
     * Return true if the currently authenticated user has the authority to edit the given site log,
     * false otherwise.
     */
    public static boolean hasAuthorityToEditSiteLog(SiteLog siteLog) {
        String fourCharId = siteLog.getSiteIdentification().getFourCharacterId();
        String siteEditAuthority = "edit-" + fourCharId.toLowerCase();
        return hasAuthority(siteEditAuthority) || hasAuthority("superuser");
    }

    /**
      * Throw an exception if the currently authenticated user does not have the authority to edit the given site log.
      */
    public static void assertAuthorityToEditSiteLog(SiteLog siteLog) throws AccessDeniedException {
        if (!hasAuthorityToEditSiteLog(siteLog)) {
            throw new AccessDeniedException("Cannot edit site " + siteLog.getSiteIdentification().getFourCharacterId());
        }
    }
}
