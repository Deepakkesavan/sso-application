package com.clarium.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/user")
    public Map<String, Object> getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> user = new HashMap<>();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) auth.getPrincipal();
            user.put("authenticated", true);
            user.put("name", oauthUser.getAttribute("name"));
            user.put("email", oauthUser.getAttribute("email"));
            user.put("sub", oauthUser.getAttribute("sub"));
            user.put("attributes", oauthUser.getAttributes());
        } else {
            user.put("authenticated", false);
        }

        return user;
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return response;
    }

    @GetMapping("/login-url")
    public Map<String, String> getLoginUrl() {
        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", "/oauth2/authorization/azure");
        return response;
    }
}
