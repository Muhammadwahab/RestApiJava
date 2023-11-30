package com.example.restapilearning.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
@Priority(999)
public class LoggedFilter implements ContainerRequestFilter {


    private String readInputStream(InputStream inputStream) {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
            return "";
        }
    }


    private String convertMapToJson(MultivaluedMap<String, String> headers) {
        // Convert MultivaluedMap to a Map for easier JSON conversion
        Map<String, Object> headersMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();
            headersMap.put(headerName, headerValues.size() == 1 ? headerValues.get(0) : headerValues);
        }

        // Convert Map to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(headersMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
            return "{}";
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String headers = convertMapToJson(requestContext.getHeaders());
        InputStream inputStream = requestContext.getEntityStream();

        requestContext.setProperty("header", headers);

        if (requestContext.hasEntity()) {
            System.out.println("Request contains a request body.");
            String requestBody = readInputStream(inputStream);
            requestContext.setEntityStream(new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8)));
            requestContext.setProperty("body", requestBody);
            requestContext.setEntityStream(new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8)));
        } else if (!requestContext.getUriInfo().getQueryParameters().isEmpty()) {   // Check if the request has query parameters
            String mapJson = convertMapToJson(requestContext.getUriInfo().getQueryParameters());
            requestContext.setProperty("body", mapJson);
            System.out.println("Request contains query parameters.");
        } else {
            requestContext.setProperty("body", "{}");
            System.out.println("Request does not contain query parameters.");
        }
    }
}

