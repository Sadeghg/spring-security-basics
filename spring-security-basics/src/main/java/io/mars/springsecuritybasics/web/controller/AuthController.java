package io.mars.springsecuritybasics.web.controller;

import io.mars.springsecuritybasics.model.dto.SecurityUserDto;
import io.mars.springsecuritybasics.model.entity.Authority;
import io.mars.springsecuritybasics.model.entity.User;
import io.mars.springsecuritybasics.service.security.JwtTokenProvider;
import io.mars.springsecuritybasics.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider provider;
    private final UserService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@AuthenticationPrincipal SecurityUserDto user){
        return ResponseEntity.ok(provider.generateJwtToken(user));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        return ResponseEntity.ok(service.save(user));
    }

}
