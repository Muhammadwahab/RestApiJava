package com.example.restapilearning.resources;

import com.example.restapilearning.configurations.AppConfig;
import com.example.restapilearning.database.User;
import com.example.restapilearning.database.UserService;
import com.example.restapilearning.responses.ApiResponse;
import com.example.restapilearning.responses.MetaResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UserResource {


//    @Inject
//    UserService userService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUsers() {
        UserService userService=new UserService();
        Gson gson=new GsonBuilder().serializeNulls().create();
        List<User> userServiceJson= userService.getAllUser();
//        List<User> userServiceJson= getAllUser();
        String jsonResponse= gson.toJson(ApiResponse.success(userServiceJson));
        return Response.ok(jsonResponse).build();
    }
}