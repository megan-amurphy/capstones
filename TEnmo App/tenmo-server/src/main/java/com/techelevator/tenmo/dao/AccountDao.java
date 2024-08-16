package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account getCurrentUserAccount(String userName);

    Account updateAccountBalance(Account account);

    List<Account> getAllAccounts();

    BigDecimal getBalance(int accountId);

    Account getAccountByUserId(int userId);
}
