package com.encoria.api.security;

import com.encoria.api.exception.ErrorResponse;
import com.encoria.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserProfileCheckFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Set<String> PROFILE_CREATION_PATTERNS = Set.of(
            "/api/profile/onboard",
            "/api/profile/check-username"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Check if the user is authenticated.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication instanceof JwtAuthenticationToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if the request is a profile creation request.
        boolean isProfileCreationRequest = PROFILE_CREATION_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
        if (isProfileCreationRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if the user has a profile.
        Jwt jwt = (Jwt) authentication.getPrincipal();
        if (userRepository.findByExternalAuthId(jwt.getSubject()).isPresent()) {
            filterChain.doFilter(request, response);
        } else {
            ErrorResponse error = ErrorResponse.of(
                    HttpStatus.FORBIDDEN,
                    "USER_PROFILE_REQUIRED",
                    request.getRequestURI()
            );
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), error);
        }
    }
}
