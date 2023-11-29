package com.example.restapilearning.database;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "api_requests")
public class ApiRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "request_url", nullable = false)
    private String requestUrl;

    @Column(name = "request_method", nullable = false)
    private String requestMethod;

    @Column(name = "request_headers", columnDefinition = "JSON")
    private String requestHeaders;

    @Column(name = "request_body", columnDefinition = "JSON")
    private String requestBody;

    @Column(name = "response_status_code")
    private Integer responseStatusCode;

    @Column(name = "response_headers", columnDefinition = "JSON")
    private String responseHeaders;

    @Column(name = "response_body", columnDefinition = "JSON")
    private String responseBody;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date timestamp;

    // Constructors, getters, and setters

}
