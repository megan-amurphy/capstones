package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    Transfer createTransfer(Transfer transfer, int fromUserid, int toUserId);

    Transfer getTransferById(int id);

    List<Transfer> getListOfTransfers(int userId);

    List<Transfer> getTransfersByUserId(int id);

    List<Transfer> getPendingRequests(int userid);

    int updateTransferStatus(int userId, int transferId, String newStatusDesc);
}
