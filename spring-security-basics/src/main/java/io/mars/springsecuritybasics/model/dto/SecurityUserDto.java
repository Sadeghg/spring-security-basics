package io.mars.springsecuritybasics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@AllArgsConstructor
public class SecurityUserDto {

    private String username;
    private String password;
    private Set<String> authorities;
}
