package io.mars.springsecuritybasics.web.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mars.springsecuritybasics.model.dto.ErrorResponse;
import io.mars.springsecuritybasics.service.security.JwtAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static io.mars.springsecuritybasics.utils.constants.SecurityConstants.*;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider authProvider;

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(AUTHORIZATION);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.substring(TOKEN_PREFIX.length());
            try {
                SecurityContextHolder.getContext().setAuthentication(authProvider.authorize(token));
            } catch (TokenExpiredException exception) {
                handleNotAuthorized(response, exception.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return servletPath.startsWith(LOGIN_URL) || servletPath.startsWith(REGISTER_URL);
    }

    private void handleNotAuthorized(HttpServletResponse response, String message) throws IOException {
        ErrorResponse error = new ErrorResponse(FORBIDDEN, message);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(SC_FORBIDDEN);
        response.getOutputStream().write(MAPPER.writeValueAsString(error).getBytes());
    }
}
