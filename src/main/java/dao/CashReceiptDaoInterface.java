package dao;

import entity.CashReceipt;

import java.sql.SQLException;
import java.util.Optional;

public interface CashReceiptDaoInterface {

    CashReceipt save(CashReceipt cashReceipt) throws SQLException;

    Optional<CashReceipt> findById(Long id) throws SQLException;
}
