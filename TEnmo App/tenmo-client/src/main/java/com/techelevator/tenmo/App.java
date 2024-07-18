package com.techelevator.tenmo;

import com.fasterxml.jackson.core.JsonToken;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.security.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.security.UserService;
import com.techelevator.tenmo.services.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    //MM instantiated AccountService class so we could call on current balance for user.
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final ConsoleService consoleService = new ConsoleService();
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            transferService.setAuthToken(currentUser.getToken());
            accountService.setAuthToken(currentUser.getToken());
            userService.setAuthToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance(currentUser.getUser().getId());
        if (balance != null) {
            System.out.println("Current Account Balance: " + balance);
        } else {
            System.out.println("An error occurred. Please try again later.");;
        }
    }
	private void viewTransferHistory() {
        List<Transfer> transfers = transferService.getAllTransfers(currentUser.getUser().getId());

        // Check if transfers are not null and not empty
        if (transfers != null && !transfers.isEmpty()) {
            // Filter out pending transfers
            transfers = transfers.stream()
                    .filter(transfer -> transfer.getTransferStatusId() != 1)
                    .collect(Collectors.toList());

            // Check if there are any non-pending transfers left
            if (!transfers.isEmpty()) {
                // Print out each transfer's details
                for (Transfer transfer : transfers) {
                    System.out.println("-------------------------------------");
                    System.out.println("Transfer ID: " + transfer.getTransferId());
                    System.out.println("Transfer Type: " + transfer.getTransferType());
                    System.out.println("Amount: " + transfer.getAmount());
                    System.out.println("Status: " + transfer.getStatus());
                    System.out.println("From Account ID: " + transfer.getFromAccountId());
                    System.out.println("To Account ID: " + transfer.getToAccountId());
                }
            } else {
                // Handle case when no non-pending transfers are found
                System.out.println("No non-pending transfer history found.");
            }
        } else {
            // Handle case when no transfers are found
            System.out.println("No transfer history found.");
        }
    }
    private void viewPendingRequests() {
        List<Transfer> pendingTransfers = transferService.getPendingRequests(currentUser.getUser().getId());

        System.out.println("Pending Transfers: ");
        for (Transfer transfer : pendingTransfers) {
            System.out.println("Transfer ID: " + transfer.getTransferId());
            System.out.println("Transfer Type: " + transfer.getTransferType());
            System.out.println("Status: " + transfer.getStatus());
            System.out.println("From Account ID: " + transfer.getFromAccountId());
            System.out.println("To Account ID: " + transfer.getToAccountId());
            System.out.println("Amount: " + transfer.getAmount());
            System.out.println("-------------------------------------");

        }
        int transferId = consoleService.promptForInt("Enter Transfer ID to approve/reject (or 0 to cancel): ");
        if (transferId != 0) {
            String action = consoleService.promptForString("Approve or Reject (A/R): ");
            if ("A".equalsIgnoreCase(action)) {
                if (transferService.approveTransfer(transferId)) {
                    System.out.println("Transfer approved.");
                } else {
                    System.out.println("Failed to approve transfer.");
                }
            } else if ("R".equalsIgnoreCase(action)) {
                if (transferService.rejectTransfer(transferId)) {
                    System.out.println("Transfer rejected.");
                } else {
                    System.out.println("Failed to reject transfer.");
                }
            } else {
                System.out.println("Invalid action.");
            }
        }
    }

    private void sendBucks () {
        //get a list of users so you know who you are sending to.

        User[] users = userService.retrieveAllUsers();
        if (users == null || users.length == 0) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("List of users:");
        for (User user : users) {
            System.out.println("User ID: " + user.getId() + ", Username: " + user.getUsername());

        }

        int fromUserId = currentUser.getUser().getId();
        int toUserId = consoleService.promptForInt("Enter recipient's account ID: ");
        if (toUserId == fromUserId) {
            System.out.println("You cannot send money from yourself.");
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to send: ");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("The amount must be greater than zero.");
            return;
        }
        // Create a new Transfer object
        Transfer transfer = new Transfer();
        transfer.setTransferType("Send");
        transfer.setStatus("pending");
        transfer.setFromAccountId(fromUserId);
        transfer.setToAccountId(toUserId);
        transfer.setAmount(amount);

        Transfer createdTransfer = transferService.createTransfer(transfer, fromUserId, toUserId);

        if (createdTransfer != null) {
            System.out.println("Transfer successfully sent.");
        } else {
            System.out.println("Failed to send transfer.");
        }
    }
    private void requestBucks() {
        User[] users = userService.retrieveAllUsers();
        if (users == null || users.length == 0) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("List of users:");
        for (User user : users) {
            System.out.println("User ID: " + user.getId() + ", Username: " + user.getUsername());

        }


        int fromUserId = currentUser.getUser().getId();  // Assuming currentUser's ID is the fromUserId
        int toUserId = consoleService.promptForInt("Enter recipient's account ID: ");
        if (toUserId == fromUserId) {
            System.out.println("You cannot request money from yourself.");
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to request: ");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("The amount must be greater than zero.");
            return;
        }
        // Create a new Transfer object
        Transfer transfer = new Transfer();
        transfer.setTransferType("Request");
        transfer.setStatus("pending");
        transfer.setFromAccountId(fromUserId);
        transfer.setToAccountId(toUserId);
        transfer.setAmount(amount);

        // Call the transferService to create the transfer
        Transfer createdTransfer = transferService.createTransfer(transfer, fromUserId, toUserId);


        if (createdTransfer != null) {
            System.out.println("Request for transfer sent.");
        } else {
            System.out.println("Failed to send request.");
        }
    }
}

