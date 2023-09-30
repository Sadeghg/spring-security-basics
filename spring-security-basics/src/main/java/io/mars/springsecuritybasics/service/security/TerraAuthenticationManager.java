package io.mars.springsecuritybasics.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TerraAuthenticationManager implements AuthenticationManager {

    private final JwtAuthenticationProvider jwtProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return jwtProvider.supports(UsernamePasswordAuthenticationToken.class)
                ? jwtProvider.authenticate(authentication)
                : authentication ;
    }
}
