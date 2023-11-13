package com.example.restapilearning.resources;

import com.example.restapilearning.annotations.NamingSecured;
import com.example.restapilearning.database.User;
import com.example.restapilearning.database.UserService;
import com.example.restapilearning.responses.ApiResponse;
import com.example.restapilearning.responses.PaginatedItems;
import com.example.restapilearning.security.CustomSecurityContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Path("/users")
public class UserResource {



    @EJB
    UserService userService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @NamingSecured
    public Response getAllUsers(@QueryParam("page") @DefaultValue("1") int page,@QueryParam("page_size") @DefaultValue("10") int pageSize) {
        Gson gson=new GsonBuilder().serializeNulls().create();
        if (page<=0){
            String json=gson.toJson( ApiResponse.error(Response.Status.BAD_REQUEST.getStatusCode(),"page number can not be less then 1",null));
            return Response.status(Response.Status.BAD_REQUEST).entity(json).build();
        }
        if (pageSize<=0){
            String json=gson.toJson( ApiResponse.error(Response.Status.BAD_REQUEST.getStatusCode(),"page size can not be less then 1",null));
            return Response.status(Response.Status.BAD_REQUEST).entity(json).build();
        }
        List<User> allUser= userService.getAllUser(page,pageSize);
        long count= userService.getItemCount();
        PaginatedItems<User> paginatedAnotherItems = new PaginatedItems<>(allUser,page,pageSize,count);
        String jsonResponse= gson.toJson(ApiResponse.success(paginatedAnotherItems));
        return Response.ok(jsonResponse).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/create")
    public Response addUser(User user) {
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
    @RolesAllowed({"admin","user"})
    @NamingSecured
    public Response getUserDetail(@Context ContainerRequestContext containerRequestContext) {
        try {
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
}