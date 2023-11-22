package com.example.restapilearning.resources;

import com.example.restapilearning.responses.ApiResponse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;

@Path("/notification")
public class PushNotificaitonResource {


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response sendNotification() {

        String envValue = System.getProperty("MY_VARIABLE");
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream(envValue);
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setProjectId("buscaro-v1").build();


            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully.");
            } else {
                System.out.println("FirebaseApp already exists. Skipping initialization.");
            }

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }


        // The topic name can be optionally prefixed with "/topics/".
//        String topic = "highScores";
        String registrationToken = "e4p7Clg2SV6KpnD55aY_Go:APA91bG8__sPF6LO_-4OMalEU19pMoAOLUGAMrfOf_nZZzK4JkJud0EtUj5sJlz8JsRte8_z6mddAWstsOmCqkZC24gr5YnwKGt82HaDc6ancGd58JQV9KLW6aar_xNuUjrCpH0EwxRB";

// See documentation on defining a message payload.
        Message message = Message.builder().putData("window", "admin_action_perform").putData("title", "hello").putData("body", "hello body")
//                .setNotification(Notification.builder().setBody("hello").setTitle("hello").build())
                .setToken(registrationToken).build();

// Send a message to the devices subscribed to the provided topic.
        String response = null;

        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonResponse = gson.toJson(ApiResponse.success(response));
        return Response.ok(jsonResponse).build();
    }
}