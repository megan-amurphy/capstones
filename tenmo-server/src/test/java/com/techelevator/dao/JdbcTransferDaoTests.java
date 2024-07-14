package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void createTransfer_creates_transfer() {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(1);
        transfer.setAmount(new BigDecimal("100.00"));

        try {
            Transfer createdTransfer = sut.createTransfer(transfer, 1001, 1002);


            Assert.assertNotNull(createdTransfer);
            Assert.assertEquals(2, createdTransfer.getTransferTypeId());
            Assert.assertEquals(1, createdTransfer.getTransferStatusId());
            Assert.assertEquals(new BigDecimal("100.00"), createdTransfer.getAmount());
        } catch (DaoException e) {
            e.printStackTrace();
            Assert.fail("DaoException thrown: " + e.getMessage());
        }
    }

    @Test(expected = DaoException.class)
    public void createTransfer_with_invalid_account_throws_exception() {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2); // 2 is the ID for 'Send'
        transfer.setTransferStatusId(1); // 1 is the ID for 'Pending'
        transfer.setAmount(new BigDecimal("100.00"));

        sut.createTransfer(transfer, -1, 1002); // Invalid fromUserId
    }

    @Test
    public void getTransferById_returns_correct_transfer() {
        Transfer transfer = sut.getTransferById(3001); // Assume 3001 is a valid transfer ID in your test DB

        Assert.assertNotNull(transfer);
        Assert.assertEquals(3001, transfer.getTransferId());
        Assert.assertEquals(2, transfer.getTransferTypeId()); // 2 is the ID for 'Send'
        Assert.assertEquals(1, transfer.getTransferStatusId()); // 1 is the ID for 'Pending'
    }

    @Test
    public void getTransferById_returns_null_for_invalid_id() {
        Transfer transfer = sut.getTransferById(-1);

        Assert.assertNull(transfer);
    }

    @Test
    public void getListOfTransfers_returns_all_transfers() {
        List<Transfer> transfers = sut.getListOfTransfers(1001);

        Assert.assertNotNull(transfers);
        Assert.assertTrue(transfers.size() > 0); // Assuming there are transfers in your test DB
    }

    @Test
    public void getTransfersByUserId_returns_correct_transfers() {
        List<Transfer> transfers = sut.getTransfersByUserId(1001); // Assume 1001 is a valid user ID in your test DB

        Assert.assertNotNull(transfers);
        Assert.assertTrue(transfers.size() > 0); // Assuming the user has transfers
    }

    @Test
    public void getTransfersByUserId_returns_empty_list_for_invalid_user_id() {
        List<Transfer> transfers = sut.getTransfersByUserId(-1);

        Assert.assertNotNull(transfers);
        Assert.assertTrue(transfers.isEmpty());
    }

    @Test
    public void getPendingRequests_returns_pending_transfers() {
        List<Transfer> transfers = sut.getPendingRequests(1001); // Assume 1001 is a valid user ID in your test DB

        Assert.assertNotNull(transfers);
        for (Transfer transfer : transfers) {
            Assert.assertEquals("Pending", transfer.getStatus());
        }
    }

    @Test
    public void updateTransferStatus_updates_status() {
        int userId = 1001; // Assume 1001 is a valid user ID in your test DB
        int transferId = 3001; // Assume 3001 is a valid transfer ID in your test DB

        int rowsAffected = sut.updateTransferStatus(userId, transferId, "Approved");

        Assert.assertEquals(1, rowsAffected);

        Transfer updatedTransfer = sut.getTransferById(transferId);
        Assert.assertEquals("Approved", updatedTransfer.getStatus());
    }

    @Test(expected = DaoException.class)
    public void updateTransferStatus_throws_exception_for_invalid_transfer() {
        sut.updateTransferStatus(1001, -1, "Approved"); // Invalid transfer ID
    }


}
