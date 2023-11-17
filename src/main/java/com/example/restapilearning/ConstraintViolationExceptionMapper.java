package com.example.restapilearning;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

// ExceptionMapper to handle exceptions and customize the response
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        StringBuilder message = new StringBuilder("Validation error(s): ");
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getMessage()).append(", ");
        }

        // Remove the trailing comma and space
        message.delete(message.length() - 2, message.length());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(message.toString())
                .build();
    }
}
