package com.bookstore.auth;

import com.bookstore.common.Routes;
import com.bookstore.config.GlobalAdviceConfig;
import com.bookstore.config.MailConfig;
import com.bookstore.jwt.JwtService;
import com.bookstore.user.User;
import com.bookstore.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final UserService userService;
    private final MailConfig mailConfig;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping(Routes.LOGIN)
    public String login() {
        return "auth/login";
    }

    @PostMapping(Routes.LOGIN)
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(required = false) String redirect,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model
    ) {
        try {
            GlobalAdviceConfig.logout(request, response);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(username);
                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(24 * 60 * 60);
                response.addCookie(cookie);
                if (redirect == null) {
                    response.sendRedirect(Routes.ROOT);
                }
                response.sendRedirect(redirect);
            }
        } catch (Exception e) {
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("hasError", true);
            return "auth/login";
        }
        return "redirect:" + Routes.LOGOUT;
    }

    @GetMapping(Routes.RESET_PASSWORD)
    public String resetPassword() {
        return "auth/reset-password";
    }

    @PostMapping(Routes.RESET_PASSWORD)
    public String resetPassword(@RequestParam String email, Model model) throws MessagingException {
        User user = userService.findByEmail(email);
        if (user != null) {
            String token = userService.getResetPasswordToken(user);
            user.setToken(token);
            userService.setResetPasswordToken(user);
            mailConfig.sentResetPasswordMail(user, token);
            model.addAttribute("header", "Check your email");
            model.addAttribute("message", "You will receive a password reset link shortly<br>in your email to get back to your account.");
            return "auth/notify";
        }
        model.addAttribute("email", email);
        model.addAttribute("hasError", true);
        return "auth/reset-password";
    }

    @GetMapping(Routes.CONFIRM_PASSWORD)
    public String confirmPassword(@RequestParam(name = "token", required = false) String token, Model model) {
        if (token != null) {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String username = decodedToken.split("-")[0];
            String expiration = decodedToken.split("-")[1];
            User user = userService.findByUsername(username);

            if (user == null || user.getToken() == null || !user.getToken().equals(token) || new Date(Long.parseLong(expiration)).before(new Date())) {
                model.addAttribute("header", "Invalid Request");
                model.addAttribute("message", "Your password reset token is no longer valid.<br>You can request for a <a href=\"/reset-password\">new password</a> reset link.");
                return "auth/notify";
            }
        }
        return "auth/confirm-password";
    }

    @PostMapping(Routes.CONFIRM_PASSWORD)
    public String confirmPassword(@RequestParam(required = false) String token, @RequestParam String password, Model model) {
        if (token == null) {
            User user = userService.findByUsername(Objects.requireNonNull(GlobalAdviceConfig.loggedUser()).getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setToken(null);
            user.setActive(true);
            userService.updatePassword(user);
            GlobalAdviceConfig.logout(request, response);
            model.addAttribute("header", "Password Updated");
            model.addAttribute("message", "Your password has been updated successfully.<br>You can now log in with your new password.");
            return "auth/notify";
        } else {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String username = decodedToken.split("-")[0];
            String expiration = decodedToken.split("-")[1];
            User user = userService.findByUsername(username);

            if (user != null && user.getToken() != null) {
                if (user.getToken().equals(token) && new Date(Long.parseLong(expiration)).after(new Date())) {
                    password = bCryptPasswordEncoder.encode(password);
                    user.setPassword(password);
                    user.setToken(null);
                    user.setActive(true);
                    userService.updatePassword(user);
                    model.addAttribute("header", "Password Updated");
                    model.addAttribute("message", "Your password has been updated successfully.<br>You can now log in with your new password.");
                    return "auth/notify";
                }
            }
        }
        model.addAttribute("header", "Invalid Request");
        model.addAttribute("message", "Your password reset token is no longer valid.<br>You can request for a <a href=\"/reset-password\">new password</a> reset link.");
        return "auth/notify";
    }

    @GetMapping(Routes.PROFILE)
    public String profile(@RequestParam String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user/profile";
    }
}
