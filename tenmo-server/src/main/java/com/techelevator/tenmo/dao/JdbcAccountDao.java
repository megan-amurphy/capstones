package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getCurrentUserAccount(String userName) {

        Account account = null;

        String sql = "SELECT account.account_id, account.user_id, account.balance FROM account " +
                "JOIN tenmo_user ON " +
                "account.user_id = tenmo_user.user_id " +
                "WHERE username = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userName);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }


        return account;
    }

    @Override
    public List<Account> getAllAccounts() {

        List<Account> accounts = new ArrayList<>();

        String sql = "SELECT account_id, user_id, balance FROM account;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Account account = mapRowToAccount(results);
                accounts.add(account);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return accounts;
    }


    @Override
    public Account updateAccountBalance(Account account) {

        String sql = "UPDATE account SET balance = ? WHERE user_id = ? AND balance <> ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, account.getBalance(), account.getUserId(), account.getBalance());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one.This might indicate the account does not exist or the balance is already up-to-date.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }

        return account;

    }

    @Override
    public BigDecimal getBalance(int accountId) {

        BigDecimal currentBalance = null;

        String sql = "SELECT account_id, user_id, balance\n" +
                "FROM account\n" +
                "WHERE account_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if (results.next()) {
                currentBalance = mapRowToAccount(results).getBalance();
            } else {
                System.out.println("No account found for account ID: " + accountId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }


        return currentBalance;

    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;

        String sql = "SELECT account.account_id, account.user_id, account.balance FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.user_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
            } else {
                System.out.println("No account found for user ID: " + userId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return account;
    }
    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
