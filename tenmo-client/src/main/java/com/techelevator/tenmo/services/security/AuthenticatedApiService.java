package com.techelevator.tenmo.services.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class AuthenticatedApiService {

    protected String baseUrl;

    protected final RestTemplate restTemplate = new RestTemplate();

    protected String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    protected HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }
    protected <T>HttpEntity<T> makeAuthEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(body, headers);
    }
}
