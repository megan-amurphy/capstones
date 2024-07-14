package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.security.AuthenticatedApiService;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransferService extends AuthenticatedApiService {
    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
        }

    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(baseUrl + "/transfer/alltransfers/" + userId, HttpMethod.GET,
                            makeAuthEntity(), Transfer[].class);
            transfers = List.of(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer getTransferById(int transferId) {
        Transfer transfer = new Transfer();
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl + "/transfer/details/" + transferId, HttpMethod.GET,
                            makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public List<Transfer> getPendingRequests(int userId){
        List<Transfer> pending = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(baseUrl + "/transfer/pending/" + userId, HttpMethod.GET,
                            makeAuthEntity(), Transfer[].class);
            pending = List.of(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pending;
    }

    public Transfer createTransfer(Transfer transfer, int fromUserId, int toUserId) {
        Transfer createdTransfer = null;
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/transfer/create_transfer")
                    .queryParam("fromUserId", fromUserId)
                    .queryParam("toUserId", toUserId)
                    .toUriString();

            transfer.setTransferStatusId(1); //setting it to pending for requesting or sending money
            if ("Send".equalsIgnoreCase(transfer.getTransferType())) {
                transfer.setTransferTypeId(2); // Type ID for "Send"
            } else if ("Request".equalsIgnoreCase(transfer.getTransferType())) {
                transfer.setTransferTypeId(1); // Type ID for "Request"
            }
            HttpEntity<Transfer> entity = makeAuthEntity(transfer);
            ResponseEntity<Transfer> response = restTemplate.postForEntity(url, entity, Transfer.class);

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                createdTransfer = response.getBody();
                System.out.println("Transfer created: " + createdTransfer);
            } else {
                System.out.println("Failed to create transfer, status code: " + response.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            BasicLogger.log("RestClientResponseException: " + e.getMessage());
            System.out.println("Response error body: " + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            BasicLogger.log("ResourceAccessException: " + e.getMessage());
        }
        return createdTransfer;
    }

// MM changed the restTemplate URL part to make it make the TransferController on the server side.
    public Transfer updateTransfer(Transfer transfer) {
        try {
            restTemplate.put(baseUrl + "/status/{transferId}" + transfer.getTransferId(), makeAuthEntity());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public boolean approveTransfer(int transferId) {
        boolean success = false;
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/transfer/approve")
                    .queryParam("transferId", transferId)
                    .toUriString();

            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, makeAuthEntity(), Void.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                success = true;
            }
        } catch (RestClientResponseException e) {
            BasicLogger.log("RestClientResponseException: " + e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log("ResourceAccessException: " + e.getMessage());
        }
        return success;
    }

    public boolean rejectTransfer(int transferId) {
        boolean success = false;
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/transfer/reject")
                    .queryParam("transferId", transferId)
                    .toUriString();

            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, makeAuthEntity(), Void.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                success = true;
            }
        } catch (RestClientResponseException e) {
            BasicLogger.log("RestClientResponseException: " + e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log("ResourceAccessException: " + e.getMessage());
        }
        return success;
    }
}


