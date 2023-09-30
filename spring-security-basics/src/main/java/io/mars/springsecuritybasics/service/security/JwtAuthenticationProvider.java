package io.mars.springsecuritybasics.service.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.mars.springsecuritybasics.model.dto.SecurityUserDto;
import io.mars.springsecuritybasics.model.security.SecurityUser;
import io.mars.springsecuritybasics.model.security.UserPassAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserPassAuthToken authToken = (UserPassAuthToken) authentication;
        SecurityUserDto userDto = (SecurityUserDto) authToken.getPrincipal();
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(username);
        if (!user.getPassword().equals(password)) throw new BadCredentialsException("wrong username or password");

        userDto = new SecurityUserDto(username, password, user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));

        return new UserPassAuthToken(userDto, null, user.getAuthorities());
    }

    public Authentication authorize(String token){
        String username = tokenProvider.getSubject(token);
        Instant expiredAt = tokenProvider.isTokenExpired(token);
        if (expiredAt.isBefore(Instant.now())) throw new TokenExpiredException("digimon", expiredAt);

        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(username);
        Set<String> authorities = tokenProvider.getAuthorities(token).stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        SecurityUserDto userDto = new SecurityUserDto(user.getUsername(), user.getPassword(), authorities);
        return new UserPassAuthToken(userDto, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}