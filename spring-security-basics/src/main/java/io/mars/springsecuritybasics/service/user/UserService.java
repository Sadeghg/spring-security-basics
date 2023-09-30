package io.mars.springsecuritybasics.service.user;

import io.mars.springsecuritybasics.model.entity.User;

import java.util.Optional;

public interface UserService {

    User findUserByUsername(String username);

    User save(User user);
}
