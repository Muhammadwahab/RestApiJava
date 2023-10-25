package com.example.restapilearning.middleware;

import com.example.restapilearning.responses.ApiResponse;
import com.example.restapilearning.responses.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class ResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        // Perform actions on the response, e.g., modify the entity

        if (responseContext.getStatus()== Response.Status.NOT_FOUND.getStatusCode()){
            Gson gson=new GsonBuilder().serializeNulls().create();
            responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
            ErrorResponse errorResponse=new ErrorResponse("api end point not found","this api is not listed in our server");
            ApiResponse<ErrorResponse> response = ApiResponse.error(responseContext.getStatus(), "Resource not found",errorResponse);
            String json=gson.toJson(response);
            responseContext.setEntity(json);
        }
    }
}
