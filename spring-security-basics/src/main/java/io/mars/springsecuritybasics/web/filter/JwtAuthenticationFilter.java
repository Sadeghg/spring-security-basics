package io.mars.springsecuritybasics.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mars.springsecuritybasics.model.dto.ErrorResponse;
import io.mars.springsecuritybasics.model.dto.SecurityUserDto;
import io.mars.springsecuritybasics.model.dto.SignInRequest;
import io.mars.springsecuritybasics.model.security.UserPassAuthToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static io.mars.springsecuritybasics.utils.constants.SecurityConstants.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final AuthenticationManager authManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (LOGIN_URL.equals(request.getServletPath())
                && HttpMethod.POST.matches(request.getMethod())) {
             SignInRequest signInRequest = new SignInRequest();
            try (InputStream stream = request.getInputStream()) {
                signInRequest = MAPPER.readValue(stream, SignInRequest.class);
            } catch (IOException e) {
                System.out.println(e);
            }

            String username = signInRequest.getUsername();
            String password = signInRequest.getPassword();
            SecurityUserDto userDto = new SecurityUserDto(
                    username, password,
                    Collections.emptySet());
            UserPassAuthToken authToken =
                    new UserPassAuthToken(userDto, password , Collections.emptyList());
            try {
                Authentication result = authManager.authenticate(authToken);
                if(!result.isAuthenticated()) {
                    handleBadCredential(response);
                    return;
                }
                SecurityContextHolder.getContext().setAuthentication(result);
            } catch (RuntimeException exception) {
                SecurityContextHolder.clearContext();
                throw exception;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith(LOGIN_URL);
    }

    private void handleBadCredential(HttpServletResponse response) throws IOException {
        String errorMessage = "username or password is wrong";
        ErrorResponse error = new ErrorResponse(UNAUTHORIZED, errorMessage);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().write(MAPPER.writeValueAsString(error).getBytes());
    }
}
