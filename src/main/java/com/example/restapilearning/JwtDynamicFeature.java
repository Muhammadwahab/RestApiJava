package com.example.restapilearning;


import com.example.restapilearning.annotations.Secured;
import com.example.restapilearning.middleware.RequestFilter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

@Priority(Priorities.AUTHENTICATION)
public class JwtDynamicFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        // Check if the resource class or method requires authentication
        if (requiresAuthentication(resourceInfo)) {
            // Register your authentication filter
            context.register(RequestFilter.class);
        }
    }

    private boolean requiresAuthentication(ResourceInfo resourceInfo) {
        // Logic to determine if authentication is required for this resource
        // You can use annotations, resource path, or any other criteria
        return resourceInfo.getResourceClass().isAnnotationPresent(Secured.class)
                || resourceInfo.getResourceMethod().isAnnotationPresent(Secured.class);
    }
}
