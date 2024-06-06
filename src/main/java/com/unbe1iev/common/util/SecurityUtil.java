package com.unbe1iev.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class SecurityUtil {

    public static final String X_DOMAIN_HEADER = "X-DOMAIN";

    public static final String CLAIM_REALM = "realm";
    private static final String CLAIM_REALM_ACCESS = "realm_access";
    private static final String REALM_ACCESS_ROLES = "roles";

    public static final String ANONYMOUS = "anonymous";
    public static final String ADMIN_ROLE = "admin";
    public static final String CREATOR_ROLE = "creator";

    private static final String CREATOR_USER = "creatorUser";

    private SecurityUtil() {
    }

    public static String getLoggedUserKeycloakId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        return principal instanceof Principal p ? p.getName() : principal.toString();
    }

    public static String getDomain() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return getRealmFromJwt(jwt);
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return (String)authentication.getCredentials();
        }
        return null;
    }

    public static String getLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            return getLoginFromJwt(jwt);
        }
        return principal instanceof Principal p ? p.getName() : principal.toString();
    }

    public static boolean isUserInRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken token) {
            Jwt jwt = token.getToken();
            JsonNode realmAccess = jwt.getClaim(CLAIM_REALM_ACCESS);
            if (realmAccess != null && realmAccess.has(REALM_ACCESS_ROLES)) {
                for (JsonNode role : realmAccess.get(REALM_ACCESS_ROLES)) {
                    if (role.asText().equals(roleName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Set<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken token) {
            return getAuthorities(token.getTokenAttributes());
        }
        return Collections.emptySet();
    }

    public static Set<String> getAuthorities(Map<String, Object> claims) {
        if (claims.get(CLAIM_REALM_ACCESS) == null || ((Map<?, ?>) claims.get(CLAIM_REALM_ACCESS)).get(REALM_ACCESS_ROLES) == null) {
            return Collections.emptySet();
        }

        JsonNode rolesNode = (JsonNode) ((Map<?, ?>) claims.get(CLAIM_REALM_ACCESS)).get(REALM_ACCESS_ROLES);
        return StreamSupport.stream(rolesNode.spliterator(), false)
                .map(JsonNode::asText)
                .collect(Collectors.toSet());
    }

    private static String getRealmFromJwt(Jwt principal) {
        return principal.getClaimAsString(CLAIM_REALM);
    }

    private static String getLoginFromJwt(Jwt principal) {
        String preferredUsername = principal.getClaimAsString("preferred_username");
        return preferredUsername != null ? preferredUsername : ANONYMOUS;
    }

    public static String getAuthorizationToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            return "Bearer " + jwt.getTokenValue();
        }
        return null;
    }

    public static void workASCreatorUser(String domain) {
        workAsUser(CREATOR_USER, domain);
    }

    private static void workAsUser(String userName) {
        workAsUser(userName, null);
    }

    private static void workAsUser(String userName, String domain) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userName, domain));
    }
}
