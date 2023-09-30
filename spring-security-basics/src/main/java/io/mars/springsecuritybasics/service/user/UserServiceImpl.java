package io.mars.springsecuritybasics.service.user;

import io.mars.springsecuritybasics.data.UserRepository;
import io.mars.springsecuritybasics.model.entity.User;
import io.mars.springsecuritybasics.model.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("no user with username: " + username));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
