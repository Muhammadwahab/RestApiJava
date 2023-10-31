package com.example.restapilearning.resources;

import com.example.restapilearning.annotations.NamingSecured;
import com.example.restapilearning.annotations.Secured;
import com.example.restapilearning.database.User;
import com.example.restapilearning.database.UserService;
import com.example.restapilearning.responses.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Path("/users")
@NamingSecured
public class UserResource {


    @Context
    private SecurityContext securityContext;
    @GET
    @Produces({MediaType.APPLICATION_JSON})
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
    public Response addUser(User user) {
        UserService userService=new UserService();
        Gson gson=new GsonBuilder().serializeNulls().create();
        String token = Jwts.builder()
                .claim("user",user)
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .compact();
        user.setJwt(token);
        user=userService.addUser(user);
        String jsonResponse= gson.toJson(ApiResponse.success(user));
        return Response.ok(jsonResponse).build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/details/{id}")
    public Response getUserDetail(@PathParam("id") Long id ) {
        System.out.println("principle "+securityContext.getUserPrincipal().getName());
        UserService userService=new UserService();
        User user=userService.getUserById(id);
        Gson gson=new GsonBuilder().serializeNulls().create();

        String jsonResponse= gson.toJson(ApiResponse.success(user));
        return Response.ok(jsonResponse).build();
    }

    public static Key generateSecretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}