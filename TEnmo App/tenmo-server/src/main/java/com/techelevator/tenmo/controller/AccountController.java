package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
public class AccountController {

    private final AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping("/{userId}/balance")
    public BigDecimal getBalance(@PathVariable int userId) {
        return accountDao.getBalance(userId);

    }

    @GetMapping("/{userId}")
    public Account getAccountByUserId(@PathVariable int userId) {
        return accountDao.getAccountByUserId(userId);
    }

    @PutMapping("/account/balance")
    public Account updatedBalance(@RequestBody Account account) {
        return accountDao.updateAccountBalance(account);
    }
}