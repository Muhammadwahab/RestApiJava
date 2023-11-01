package com.example.restapilearning.resources;

import com.example.restapilearning.annotations.NamingSecured;
import com.example.restapilearning.database.User;
import com.example.restapilearning.database.UserService;
import com.example.restapilearning.responses.ApiResponse;
import com.example.restapilearning.security.CustomSecurityContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Path("/users")
public class UserResource {


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @NamingSecured
    public Response getAllUsers() {
        UserService userService=new UserService();
        Gson gson=new GsonBuilder().serializeNulls().create();
        List<User> userServiceJson= userService.getAllUser();
//        List<User> userServiceJson= getAllUser();
        String jsonResponse= gson.toJson(ApiResponse.success(userServiceJson));

        return Response.ok(jsonResponse).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/create")
    @Transactional
    public Response addUser(User user) {
        UserService userService=new UserService();
        Gson gson=new GsonBuilder().serializeNulls().create();
        user=userService.addUser(user);
        String token = Jwts.builder()
                .claim("user",user)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().atZone(ZoneId.systemDefault()).plusYears(2).toInstant())) // Set expiration to 2 years from now
                .compact();
        user.setJwt(token);
        user=userService.updateUser(user);
        String jsonResponse= gson.toJson(ApiResponse.success(user));
        return Response.ok(jsonResponse).build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/details")
    @NamingSecured
    public Response getUserDetail(@Context ContainerRequestContext containerRequestContext) {
        try {
            UserService userService=new UserService();
            CustomSecurityContext customSecurityContext= (CustomSecurityContext) containerRequestContext.getSecurityContext();
            User user=userService.getUserById(customSecurityContext.getUser().getId());
            Gson gson=new GsonBuilder().serializeNulls().create();

            String jsonResponse= gson.toJson(ApiResponse.success(user));
            return Response.ok(jsonResponse).build();
        }catch (Exception e)
        {
            throw new InternalServerErrorException(e);
        }

    }

    public static Key generateSecretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}