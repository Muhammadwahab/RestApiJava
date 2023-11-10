package com.example.restapilearning.resources;

import com.example.restapilearning.configurations.AppConfig;
import com.example.restapilearning.database.CalculatorService;
import com.example.restapilearning.responses.ApiResponse;
import com.example.restapilearning.responses.MetaResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/meta")
public class MetaResource {


    @EJB
    CalculatorService calculatorService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMetaData() {
        calculatorService.add(1,2);
        MetaResponse metaResponse =new MetaResponse("hello cat");
        metaResponse.setName( AppConfig.getDbUrl());
        Gson gson=new GsonBuilder().serializeNulls().create();
        String jsonResponse= gson.toJson(ApiResponse.success(metaResponse));
        return Response.ok(jsonResponse).build();
    }
}