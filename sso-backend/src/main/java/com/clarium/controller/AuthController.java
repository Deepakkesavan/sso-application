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

    /**
     * Endpoint 1: Get current user information
     * Returns user details if authenticated, otherwise returns unauthenticated status
     */
    @GetMapping("/user")
    public Map<String, Object> getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) auth.getPrincipal();

            Map<String, Object> user = new HashMap<>();
            user.put("id", oauthUser.getAttribute("sub"));
            user.put("name", oauthUser.getAttribute("name"));
            user.put("email", oauthUser.getAttribute("email"));
            user.put("givenName", oauthUser.getAttribute("given_name"));
            user.put("familyName", oauthUser.getAttribute("family_name"));
            user.put("picture", oauthUser.getAttribute("picture"));

            response.put("authenticated", true);
            response.put("user", user);
        } else {
            response.put("authenticated", false);
            response.put("user", null);
        }

        return response;
    }

    /**
     * Endpoint 2: Get login URL for Azure AD
     * Returns the OAuth2 authorization URL
     */
    @GetMapping("/login-url")
    public Map<String, String> getLoginUrl() {
        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", "/oauth2/authorization/azure");
        response.put("message", "Redirect to this URL to start Azure AD authentication");
        return response;
    }

    /**
     * Endpoint 3: Logout endpoint
     * Clears the security context and invalidates the session
     */
    @GetMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        try {
            // Clear security context
//            SecurityContextHolder.clearContext();

            // Invalidate session if it exists
            if (request.getSession(false) != null) {
                request.getSession().invalidate();
                result.put("success", true);
                result.put("message", "Logged out successfully");
            } else {
                result.put("success", false);
                result.put("message", "Not Logged Out successfully");
            }



        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during logout: " + e.getMessage());
        }

        return result;
    }

    /**
     * Success callback after Azure AD authentication
     */
    @GetMapping("/success")
    public Map<String, Object> loginSuccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Successfully authenticated with Azure AD");
        return response;
    }

    /**
     * Failure callback after Azure AD authentication
     */
    @GetMapping("/failure")
    public Map<String, Object> loginFailure() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Authentication failed");
        return response;
    }

    /**
     * Logout success callback
     */
    @GetMapping("/logout-success")
    public Map<String, Object> logoutSuccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Successfully logged out");
        return response;
    }
}