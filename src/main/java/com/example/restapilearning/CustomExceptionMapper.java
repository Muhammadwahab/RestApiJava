package com.example.restapilearning;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

// ExceptionMapper to handle exceptions and customize the response
@Provider
public class CustomExceptionMapper implements ExceptionMapper<InternalServerErrorException> {

    @Override
    public Response toResponse(InternalServerErrorException exception) {


        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();
        // Set a custom error message and HTTP status code

        return Response.status(exception.getResponse().getStatus())
                .entity(stackTrace)
                .build();
    }
}
