package com.example.restapilearning.application;

import com.example.restapilearning.ConstraintViolationExceptionMapper;
import com.example.restapilearning.CustomExceptionMapper;
import com.example.restapilearning.middleware.LoggedFilter;
import com.example.restapilearning.middleware.RequestFilter;
import com.example.restapilearning.middleware.ResponseFilter;
import com.example.restapilearning.resources.MetaResource;
import com.example.restapilearning.resources.PushNotificaitonResource;
import com.example.restapilearning.resources.UserResource;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
@DeclareRoles({"admin", "user"})
public class HelloApplication extends ResourceConfig {


    public HelloApplication() {
        super();

        packages("com.example.restapilearning.resources");

        // Register your filter classes
        register(UserResource.class);
        register(MetaResource.class);
        register(PushNotificaitonResource.class);
        register(RequestFilter.class);


        register(MultiPartFeature.class); // Register MultiPartFeature
        register(ResponseFilter.class);
        register(LoggedFilter.class);
        register(RolesAllowedDynamicFeature.class);
        register(CustomExceptionMapper.class);
        register(ConstraintViolationExceptionMapper.class);


    }
}