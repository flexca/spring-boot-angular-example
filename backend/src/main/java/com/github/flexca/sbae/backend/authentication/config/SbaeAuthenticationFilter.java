package com.github.flexca.sbae.backend.authentication.config;

import com.github.flexca.sbae.backend.authentication.model.SbaeAuthentication;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.github.flexca.sbae.backend.authentication.service.JwtService;
import com.github.flexca.sbae.backend.users.model.UserDto;
import com.github.flexca.sbae.backend.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SbaeAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.isBlank(authorization) || !authorization.startsWith(BEARER_PREFIX)) {
            SecurityContextHolder.getContext().setAuthentication(new SbaeAuthentication(guestUser()));
        } else {
            try {
                String token = authorization.substring(BEARER_PREFIX.length());
                Claims claims = jwtService.extractAllClaims(token);
                String userId = claims.getSubject();
                UserDto user = userService.getById(userId);
                SecurityContextHolder.getContext().setAuthentication(new SbaeAuthentication(user));
            } catch (Exception e) {
                SecurityContextHolder.getContext().setAuthentication(new SbaeAuthentication(guestUser()));
            }
        }

        filterChain.doFilter(request, response);
    }

    private UserDto guestUser() {
        UserDto user = new UserDto();
        return user;
    }
}
