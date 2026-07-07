package com.example.E_Commerce.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws IOException, ServletException {
        final String authHeader =request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 3. Extract JWT
        String jwt = authHeader.substring(7);

        // 4. Extract username from JWT
        String username = jwtService.extractUsername(jwt);

        // 5. If user is not already authenticated
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Load latest user from database
            UserDetails userDetails =
                    customUserDetailService.loadUserByUsername(username);

            // 7. Validate token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. Create Authentication object
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 9. Add request details
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 10. Store Authentication in SecurityContext
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }
        }

        // 11. Continue to next filter
        filterChain.doFilter(request, response);
    }

}

