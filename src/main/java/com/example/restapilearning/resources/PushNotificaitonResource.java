package com.example.restapilearning.resources;

import com.example.restapilearning.configurations.AppConfig;
import com.example.restapilearning.responses.ApiResponse;
import com.example.restapilearning.responses.MetaResponse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Path("/notification")
public class PushNotificaitonResource {


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response sendNotification() {

        String envValue=System.getenv("ASADMIN_LISTENER_PORT");
//
//        FileInputStream serviceAccount =
//                null;
//        try {
//            serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (IOException e) {
//            throw new InternalServerErrorException(e);
//        }


        MetaResponse metaResponse =new MetaResponse("hello cat");
        metaResponse.setName( AppConfig.getDbUrl());
        Gson gson=new GsonBuilder().serializeNulls().create();
        String jsonResponse= gson.toJson(ApiResponse.success(envValue));
        return Response.ok(jsonResponse).build();
    }
}