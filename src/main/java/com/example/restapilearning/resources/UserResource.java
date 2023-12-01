package com.example.restapilearning.resources;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.restapilearning.annotations.NamingSecured;
import com.example.restapilearning.database.Roles;
import com.example.restapilearning.database.RoleService;
import com.example.restapilearning.database.User;
import com.example.restapilearning.database.UserService;
import com.example.restapilearning.responses.ApiResponse;
import com.example.restapilearning.responses.PaginatedItems;
import com.example.restapilearning.security.CustomSecurityContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Jwts;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Path("/users")
public class UserResource {



    @EJB
    UserService userService;

    @EJB
    RoleService roleService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @NamingSecured
    public Response getAllUsers(@QueryParam("page") @DefaultValue("1") int page,@QueryParam("page_size") @DefaultValue("10") int pageSize) {
       try{
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
       }catch (Exception e){
           throw new InternalServerErrorException(e);
       }

    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/create")
    public Response addUser(@Valid User user) {
        Gson gson=new GsonBuilder().serializeNulls().create();
        Roles role=roleService.getRole("ADMIN");
        user=userService.addUser(user);
        String token = Jwts.builder()
                .claim("user",user)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().atZone(ZoneId.systemDefault()).plusYears(2).toInstant())) // Set expiration to 2 years from now
                .compact();
        user.setJwt(token);
        user.getRoles().add(role);
        role.setUsers(new HashSet<User>());
        role.getUsers().add(user);
        user=userService.updateUser(user);
        role=roleService.updateRoles(role);
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


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/upload")
    @RolesAllowed({"admin","user"})
    @NamingSecured
    public Response uploadImage( @FormDataParam("file") InputStream fileInputStream) throws IOException {
        Gson gson=new GsonBuilder().serializeNulls().create();


        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dpiluws3z",
                "api_key", "185872825672138",
                "api_secret", "VO27563L97j6HHg2iKSPOSaavx8"));



        String envValue = System.getProperty("IMAGE_FOLDER");

        String base64=convertInputStreamToBase64(fileInputStream);

        byte[] imageBytes = Base64.getDecoder().decode(base64);




      //  File outputFile = convertInputStreamToFile(fileInputStream,envValue+"wahab.png");


        Map uploadResult = cloudinary.uploader().
                upload(imageBytes, ObjectUtils.emptyMap());



        // Process the Cloudinary upload result as needed
        String publicUrl = (String) uploadResult.get("secure_url");

        String jsonResponse= gson.toJson(ApiResponse.success(publicUrl));
        return Response.ok(jsonResponse).build();
    }

    public static String convertInputStreamToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] data = outputStream.toByteArray();
        byte[] base64Encoded = Base64.getEncoder().encode(data);

        String dataa= new String(base64Encoded);
        System.out.println("base 64 is "+dataa);
        return dataa;

    }

    public static File convertInputStreamToFile(InputStream inputStream, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return outputFile;
    }


    public static File convertInputStreamToFileInMemory(InputStream inputStream, String envValue) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] data = outputStream.toByteArray();
        return new ByteArrayFile(data, envValue+"wahab.png");
    }

    private static class ByteArrayFile extends File {
        private final byte[] data;

        public ByteArrayFile(byte[] data, String name) {
            super(name);
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }
    }

}