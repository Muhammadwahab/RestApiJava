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

        if (responseContext.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
            ErrorResponse errorResponse = new ErrorResponse("api end point not found", "this api is not listed in our server");
            ApiResponse<ErrorResponse> response = ApiResponse.error(responseContext.getStatus(), "Resource not found", errorResponse);
            String json = gson.toJson(response);
            responseContext.setEntity(json);
        } else if (responseContext.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", responseContext.getEntity().toString());
            ApiResponse<ErrorResponse> response = ApiResponse.error(responseContext.getStatus(), "Internal Server Error", errorResponse);
            String json = gson.toJson(response);
            responseContext.setEntity(json);
        }
        else if (responseContext.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", responseContext.getEntity().toString());
            ApiResponse<ErrorResponse> response = ApiResponse.error(responseContext.getStatus(), "Bad Request", errorResponse);
            String json = gson.toJson(response);
            responseContext.setEntity(json);
        }
        else if (responseContext.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
            ErrorResponse errorResponse = new ErrorResponse("UnAuthorized", responseContext.getEntity().toString());
            ApiResponse<ErrorResponse> response = ApiResponse.error(responseContext.getStatus(), "UnAuthorized", errorResponse);
            String json = gson.toJson(response);
            responseContext.setEntity(json);
        }
        else if (responseContext.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
            ErrorResponse errorResponse = new ErrorResponse("Forebidin","The server understood the request but refuses to authorize it.");
            ApiResponse<ErrorResponse> response = ApiResponse.error(responseContext.getStatus(), "Forebidin", errorResponse);
            String json = gson.toJson(response);
            responseContext.setEntity(json);
        }
    }

}
