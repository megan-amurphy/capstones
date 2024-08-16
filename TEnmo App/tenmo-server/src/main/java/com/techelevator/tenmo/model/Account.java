package com.techelevator.tenmo.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;

    public Account() {
    }

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Reduces the balance of this account and increases the balance of accountTo by amount specified in amountToTransfer parameter
     * @param accountTo
     * @param amountToTransfer
     */
    public void transfer(Account accountTo, BigDecimal amountToTransfer) {
        if (this.balance.compareTo(amountToTransfer) >= 0) {
            this.balance = this.balance.subtract(amountToTransfer);
            accountTo.balance = accountTo.balance.add(amountToTransfer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, amountToTransfer+" exceeds the remaining balance of "+this.balance);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId &&
                userId == account.userId &&
                balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, userId, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
