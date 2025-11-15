package com.bookstore.config;

import com.bookstore.auth.UserPrincipleDetails;
import com.bookstore.common.Routes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;

@ControllerAdvice
public class GlobalAdviceConfig {

    @ModelAttribute(name = "uri")
    public String uri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute(name = "loggedUser")
    public UserPrincipleDetails userPrincipleDetails() {
        return loggedUser();
    }

    @ExceptionHandler(Exception.class)
    public void error(HttpServletResponse response) throws IOException {
        response.sendRedirect(Routes.ERROR);
    }

    public static UserPrincipleDetails loggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
            && authentication.isAuthenticated()
            && authentication.getPrincipal() instanceof UserPrincipleDetails userPrincipleDetails) {
            return userPrincipleDetails;
        }
        return null;
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        request.getSession().invalidate();
    }
}
