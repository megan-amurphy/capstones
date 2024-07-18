package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getCurrentUserAccount_given_valid_user_returns_account() {
        Account account = sut.getCurrentUserAccount("user1");

        Assert.assertNotNull(account);
        Assert.assertEquals(1001, account.getUserId());
        Assert.assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    public void getCurrentUserAccount_given_invalid_user_returns_null() {
        Account account = sut.getCurrentUserAccount("invalid");

        Assert.assertNull(account);
    }

    @Test
    public void getAllAccounts_returns_all_accounts() {
        List<Account> accounts = sut.getAllAccounts();

        Assert.assertNotNull(accounts);
        Assert.assertEquals(3, accounts.size());
    }

    @Test
    public void updateAccountBalance_updates_balance() {
        // Create or fetch an existing account with userId 1001 from the database
        Account account = sut.getAccountByUserId(1001);

        // Update the balance of the fetched account
        account.setBalance(new BigDecimal("5000.00"));
        Account updatedAccount = sut.updateAccountBalance(account);

        Assert.assertNotNull(updatedAccount);
        Assert.assertEquals(new BigDecimal("5000.00"), updatedAccount.getBalance());

        // Verify the updated balance in the database
        Account dbAccount = sut.getAccountByUserId(1001);
        Assert.assertEquals(new BigDecimal("5000.00"), dbAccount.getBalance());
    }

    @Test
    public void getBalance_returns_correct_balance() {
        BigDecimal balance = sut.getBalance(1001);

        Assert.assertNotNull(balance);
        Assert.assertEquals(new BigDecimal("1000.00"), balance);
    }

    @Test
    public void getAccountByUserId_given_valid_user_id_returns_account() {
        Account account = sut.getAccountByUserId(1001);

        Assert.assertNotNull(account);
        Assert.assertEquals(1001, account.getUserId());
        Assert.assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    public void getAccountByUserId_given_invalid_user_id_returns_null() {
        Account account = sut.getAccountByUserId(-1);

        Assert.assertNull(account);
    }

    @Test(expected = DaoException.class)
    public void updateAccountBalance_with_invalid_user_id_throws_exception() {
        Account account = new Account();
        account.setUserId(-1); // Invalid user ID
        account.setBalance(new BigDecimal("500.00"));

        sut.updateAccountBalance(account);
    }

    @Test
    public void updateAccountBalance_with_existing_balance_throws_exception() {
        Account account = new Account();
        account.setUserId(1001); // Assume 1001 is a valid user ID in your test DB
        account.setBalance(new BigDecimal("1000.00")); // Assuming the balance is already 1000.00 in DB

        try {
            sut.updateAccountBalance(account);
            Assert.fail("Updated balance incorrect.");
        } catch (DaoException e) {
            Assert.assertTrue(true);

        } catch (Exception e) {
            Assert.fail("Wrong exception.");
        }
    }
}


