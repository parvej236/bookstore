package com.bookstore.user;

import com.bookstore.config.MailConfig;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final MailConfig mailConfig;
    private final UserRepository repository;

    public User createUser(User user) throws MessagingException {
        if (user.getId() == null) {
           String token = getResetPasswordToken(user);
            user.setToken(token);
            user = repository.save(user);
            mailConfig.userCreateMail(user, token);
        } else {
            User usr = getById(user.getId());
            user.setPassword(usr.getPassword());
            user.setToken(usr.getToken());
            repository.save(user);
        }
        return user;
    }

    public void updatePassword(User user) {
        User usr = getById(user.getId());
        usr.setPassword(user.getPassword());
        repository.save(usr);
    }

    public void setResetPasswordToken(User user) {
        repository.save(user);
    }

    public List<User> getUserList() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void validateUser(User user, BindingResult result) {
        User usr = findByUsername(user.getUsername());
        if (usr != null && !user.getId().equals(usr.getId())) {
            result.rejectValue("username", null, "Username is already taken!");
        }

        usr = findByEmail(user.getEmail());
        if (usr != null && !user.getId().equals(usr.getId())) {
            result.rejectValue("email", null, "Email is already exist!");
        }
    }

    public String getResetPasswordToken(User user) {
        String token = user.getUsername() + "-" + (System.currentTimeMillis() + 1000 * 60 * 60 * 24) + "-" + UUID.randomUUID();
        return Base64.getEncoder().encodeToString(token.getBytes());
    }
}
