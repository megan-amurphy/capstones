package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createTransfer(Transfer transfer, int fromUserId, int toUserId) {
        Transfer createdTransfer = null;
        String sql = "INSERT INTO transfer (" +
                "    transfer_type_id, transfer_status_id, account_from, account_to, amount" +
                ") VALUES (?, ?, " +
                "(SELECT account_id FROM account WHERE user_id = ?), " +
                "(SELECT account_id FROM account WHERE user_id = ?), ?) " +
                "RETURNING transfer_id;";
        try {
            int transferID = jdbcTemplate.queryForObject(sql, Integer.class,
                    transfer.getTransferTypeId(),
                    transfer.getTransferStatusId(),
                    fromUserId,
                    toUserId,
                    transfer.getAmount());
            createdTransfer = getTransferById(transferID);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return createdTransfer;
    }

    @Override
    public Transfer getTransferById(int id) {

        Transfer transfer = null;

        String sql = "SELECT transfer_id,transfer.transfer_status_id, transfer_type.transfer_type_desc, \n" +
                "transfer_type.transfer_type_id, transfer_status.transfer_status_desc, transfer.account_from,\n" +
                "transfer.account_to, transfer.amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "WHERE transfer.transfer_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfer;

    }

    @Override
    public List<Transfer> getListOfTransfers(int userId) {

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT transfer.transfer_id, transfer.transfer_status_id, transfer_type.transfer_type_desc, \n" +
                "transfer_type.transfer_type_id, transfer_status.transfer_status_desc, transfer.account_from, \n" +
                "transfer.account_to, transfer.amount \n" +
                "FROM transfer \n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account AS account_from ON transfer.account_from = account_from.account_id\n" +
                "JOIN account AS account_to ON transfer.account_to = account_to.account_id\n" +
                "WHERE account_from.user_id = ? OR account_to.user_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByUserId(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer.transfer_id,\n" +
                "transfer.transfer_status_id,\n" +
                "transfer_type.transfer_type_desc,\n" +
                "transfer_type.transfer_type_id,\n" +
                "transfer_status.transfer_status_desc,\n" +
                "transfer.account_from,\n" +
                "transfer.account_to,\n" +
                "transfer.amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account ON account.account_id = transfer.account_from OR account.account_id = transfer.account_to\n" +
                "WHERE account.user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getPendingRequests(int userid) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT \n" +
                "transfer.transfer_id,\n" +
                "transfer.transfer_status_id,\n" +
                "transfer_type.transfer_type_desc,\n" +
                "transfer_type.transfer_type_id,\n" +
                "transfer_status.transfer_status_desc,\n" +
                "transfer.account_from,\n" +
                "transfer.account_to,\n" +
                "transfer.amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account ON account.account_id = transfer.account_from OR account.account_id = transfer.account_to\n" +
                "WHERE account.user_id = ? \n" +
                "AND transfer_status.transfer_status_desc = 'Pending';";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userid);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfers;

    }

    @Override
    public int updateTransferStatus(int userId, int transferId, String newStatusDesc) {
        String selectQuery = "SELECT COUNT(*) " +
                "FROM transfer " +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "JOIN account ON account.account_id = transfer.account_from OR account.account_id = transfer.account_to " +
                "WHERE transfer.transfer_id = ? " +
                "AND (transfer_status.transfer_status_desc = 'Pending' OR transfer_status.transfer_status_desc = 'Rejected')";

        String updateQuery = "UPDATE transfer " +
                "SET transfer_status_id = (SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?) " +
                "WHERE transfer_id = ?";

        int numberOfRows = 0;
        try {
            // Check if the transfer can be updated
            Integer transferIdResult = jdbcTemplate.queryForObject(selectQuery, Integer.class, transferId);

            if (transferIdResult != null && transferIdResult > 0) {
                // Update the transfer status
                numberOfRows = jdbcTemplate.update(updateQuery, newStatusDesc, transferId);
            } else {
                throw new DaoException("Transfer not found or status not eligible for update.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Transfer not found or status not eligible for update.", e);
        }
        return numberOfRows;
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet) {

        Transfer transfer = new Transfer();

        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferType(rowSet.getString("transfer_type_desc"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setStatus(rowSet.getString("transfer_status_desc"));
        transfer.setFromAccountId(rowSet.getInt("account_from"));
        transfer.setToAccountId(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
    }

}



