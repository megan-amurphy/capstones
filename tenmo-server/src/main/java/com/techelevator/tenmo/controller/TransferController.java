package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.security.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final TransferDao transferDao;
    private final UserDao userDao;
    private final AccountDao accountDao;


    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create_transfer")
    public ResponseEntity<Transfer> createTransfer
            (@RequestBody Transfer transfer, @RequestParam int fromUserId,
             int toUserId) {
        // Ensure valid transfer status and type IDs
        if (transfer.getTransferStatusId() == 0) {
            transfer.setTransferStatusId(1); // Set it to pending.
        }

        if (transfer.getTransferTypeId() == 0) {
            if ("Send".equalsIgnoreCase(transfer.getTransferType())) {
                transfer.setTransferTypeId(2); // Type ID for "Send"
            } else if ("Request".equalsIgnoreCase(transfer.getTransferType())) {
                transfer.setTransferTypeId(1); // Type ID for "Request"
            }
        }
        transfer.setAmount(BigDecimal.valueOf(transfer.getAmount().doubleValue()));

        Transfer createdTransfer = transferDao.createTransfer(transfer, fromUserId, toUserId);
        return new ResponseEntity<>(createdTransfer, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/approve")
    public ResponseEntity<Void> approveTransfer(@RequestParam int transferId, @RequestParam int fromUserId, @RequestParam int toUserId) {
        try {
            Transfer transfer = transferDao.getTransferById(transferId);
            User fromUser = userDao.getUserById(fromUserId); // Fetching fromUser
            User toUser = userDao.getUserById(toUserId); // Fetching toUser

            if (transfer == null || !"pending".equalsIgnoreCase(transfer.getStatus())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Account fromAccount = accountDao.getAccountByUserId(fromUserId);
            Account toAccount = accountDao.getAccountByUserId(toUserId);

            if (fromAccount == null || toAccount == null) {
                System.out.println("Error: One or both accounts not found.");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            BigDecimal fromUserBalance = fromAccount.getBalance();
            if (fromUserBalance == null) {
                System.out.println("Error: Balance for account ID " + fromAccount);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (fromUserBalance.compareTo(transfer.getAmount()) < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            fromAccount.setBalance(fromUserBalance.subtract(transfer.getAmount()));
            accountDao.updateAccountBalance(fromAccount);

            toAccount.setBalance(toAccount.getBalance().add(transfer.getAmount()));
            accountDao.updateAccountBalance(toAccount);

            transferDao.updateTransferStatus(transfer.getFromAccountId(), transferId, "approved");
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace to log
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/reject")
    public ResponseEntity<Void> rejectTransfer(@RequestParam int transferId) {
        try {
            Transfer transfer = transferDao.getTransferById(transferId);
            if (transfer == null || !"pending".equalsIgnoreCase(transfer.getStatus())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            transferDao.updateTransferStatus(transfer.getFromAccountId(), transferId, "Rejected");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace to log
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/alltransfers/{userId}")
    public List<Transfer> getListOfTransfers(@PathVariable int userId) {
        List<Transfer> transfers = transferDao.getListOfTransfers(userId);
        return transfers;
    }



    @GetMapping("/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
        List<Transfer> transfers = transferDao.getTransfersByUserId(userId);
        return transfers;
    }

    @GetMapping("/details/{transferId}")
    public Transfer getTransferById(@PathVariable int transferId) {
        return transferDao.getTransferById(transferId);
    }

    //MM- I cannot get this to run without permitting all.
    // I definitely want to learn how to properly authorize so only the user can view their own
    //pending requests.
    @PreAuthorize("permitAll")
    @GetMapping("/pending/{userId}")
    public List<Transfer> getPendingRequests(@PathVariable int userId) {
        List<Transfer> pendingTransfers = transferDao.getPendingRequests(userId);
        return pendingTransfers;
    }



    @PutMapping("/status/{transferId}")
    public int updateTransferStatus(@PathVariable int userId,
                                    @RequestParam int transferId,
                                    @RequestParam String newStatusDesc) {
        return transferDao.updateTransferStatus(userId, transferId, newStatusDesc);
    }
}