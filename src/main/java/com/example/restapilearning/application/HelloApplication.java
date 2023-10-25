package com.example.restapilearning.application;

import com.example.restapilearning.middleware.RequestFilter;
import com.example.restapilearning.middleware.ResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {


    public HelloApplication() {
        super();

        packages("com.example.restapilearning.resources");

        // Register your filter classes
        register(RequestFilter.class);

        register(ResponseFilter.class);


    }
}