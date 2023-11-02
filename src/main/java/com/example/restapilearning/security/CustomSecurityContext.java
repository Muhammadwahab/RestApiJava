package com.example.restapilearning.security;

import com.example.restapilearning.database.User;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class CustomSecurityContext implements SecurityContext {

    private  User user;

    public CustomSecurityContext(User user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {

        return user::getName;
        // Assuming User has a getUsername() method
    }

    @Override
    public boolean isUserInRole(String role) {
        // Implement your role-checking logic based on the roles of the User object

        return role.equals("admin");
    }

    @Override
    public boolean isSecure() {
        // Implement security checks if needed
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        // Return the authentication scheme used
        return HttpHeaders.AUTHORIZATION;
    }

    public User getUser() {
        return user;
    }


}