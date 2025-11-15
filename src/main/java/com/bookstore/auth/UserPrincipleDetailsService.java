package com.bookstore.auth;

import com.bookstore.user.User;
import com.bookstore.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class UserPrincipleDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService userService;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (Objects.isNull(user)) {
            System.err.println("user not found!");
            throw new UsernameNotFoundException("user not found!");
        }
        return new UserPrincipleDetails(user);
    }
}
