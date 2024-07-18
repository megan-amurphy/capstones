package com.techelevator.tenmo.services.security;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

public class UserService extends AuthenticatedApiService {
    
    public UserService(String baseUrl) {
        this.baseUrl = baseUrl; 
    }
    
    public User[] retrieveAllUsers() {
        User[] users = null;
        try {
            Transfer transfer = new Transfer();
            ResponseEntity<User[]> response =
                    restTemplate.exchange(baseUrl + "/user/users", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }
}
   
