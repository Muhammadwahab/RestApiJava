package com.example.restapilearning.middleware;

import com.example.restapilearning.annotations.NamingSecured;
import com.example.restapilearning.annotations.Secured;
import com.example.restapilearning.database.User;
import com.example.restapilearning.security.CustomSecurityContext;
import com.google.gson.Gson;
import io.jsonwebtoken.*;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
@NamingSecured
@Priority(Priorities.AUTHENTICATION)
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

                String json= claimsJwt.getPayload().get("user").toString();
                Gson gson= new Gson();
                User user=gson.fromJson(json,User.class);
                containerRequestContext.setSecurityContext(new CustomSecurityContext(user));
//                containerRequestContext.abortWith(Response.status(200).entity("token is "+tokenValue).build());

            }
            else{
                containerRequestContext.abortWith(Response.status(401).entity(Response.Status.UNAUTHORIZED.toString()).build());
            }
         //   System.out.println("token is "+token);


        }catch (ExpiredJwtException exception){

            throw new InternalServerErrorException("Jwt token expired");

        }catch (Exception exception){

            throw new InternalServerErrorException(exception);

        }

        Logger.getLogger("test filter "+RequestFilter.class.getName());
    }
}
