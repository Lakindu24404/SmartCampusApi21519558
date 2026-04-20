// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.security;

import com.smartcampus.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter { // intercept requests to check jwt

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // check if header is there
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            // validate token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(AuthService.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            final String role = (String) claims.get("role");

            // set security context for @RolesAllowed or manual checks
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() { return () -> username; }

                @Override
                public boolean isUserInRole(String r) { return role != null && role.equals(r); }

                @Override
                public boolean isSecure() { return false; }

                @Override
                public String getAuthenticationScheme() { return "Bearer"; }
            });

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
