package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.security.AuthenticatedApiService;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.List;

public class AccountService extends AuthenticatedApiService {


    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Account getAccountByUserId(int userId) {
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + userId, HttpMethod.GET,
                            makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(baseUrl + userId + "/balance", HttpMethod.GET,
                            makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }


    public void updateBalance(int accountId, BigDecimal newBalance) {
        String url = baseUrl + "account/balance";
        Account account = new Account();
        account.setAccountId(accountId);
        account.setBalance(newBalance);
        HttpEntity<Account> entity = makeAuthEntity(account);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public List<Account> getListOfAccounts() {
        List<Account> account = null;
        try {
            ResponseEntity<Account[]> response =
                    restTemplate.exchange(baseUrl + "/user", HttpMethod.GET,
                            makeAuthEntity(), Account[].class);
            account = List.of(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

}