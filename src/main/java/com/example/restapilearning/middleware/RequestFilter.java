package com.example.restapilearning.middleware;

import com.example.restapilearning.annotations.NamingSecured;
import com.example.restapilearning.annotations.Secured;
import com.example.restapilearning.database.User;
import io.jsonwebtoken.*;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
@NamingSecured
public class RequestFilter implements ContainerRequestFilter {


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        try
        {
            String token = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

            // Validate the token
            if (token != null && token.startsWith("Bearer ")){
                String tokenValue = token.substring(7); // Remove "Bearer "


                Jwt<Header, Claims> claimsJwt = Jwts.parser().unsecured().build().parseUnsecuredClaims(tokenValue)
                        ;

                User user= (User) claimsJwt.getPayload().get("user");
                containerRequestContext.abortWith(Response.status(200).entity("token is "+tokenValue).build());

            }
            else{
                containerRequestContext.abortWith(Response.status(401).entity(Response.Status.UNAUTHORIZED.toString()).build());
            }
         //   System.out.println("token is "+token);


        }catch (Exception exception){

            throw new InternalServerErrorException(exception);

        }

        Logger.getLogger("test filter "+RequestFilter.class.getName());
    }
}
