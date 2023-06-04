package dao.impl;

import dao.AbstractDao;
import dao.CashReceiptDaoInterface;
import entity.CashReceipt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CashReceiptDao extends AbstractDao implements CashReceiptDaoInterface {

    private static final String SAVE = "INSERT INTO cash_receipt (barcode, amount,  cashier, date, discount, total_amount, card_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID = "SELECT * FROM cash_receipt WHERE id = ?";

    @Override
    public CashReceipt save(CashReceipt cashReceipt) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, cashReceipt.getBarcode());
            preparedStatement.setBigDecimal(2, cashReceipt.getAmount());
            preparedStatement.setString(3, cashReceipt.getCashier());
            preparedStatement.setDate(4, new Date(cashReceipt.getDate().getTime()));
            preparedStatement.setBigDecimal(5, cashReceipt.getDiscount());
            preparedStatement.setBigDecimal(6, cashReceipt.getTotalAmount());
            preparedStatement.setLong(7, cashReceipt.getCard().getId());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                cashReceipt.setId((long) resultSet.getInt(1));
            }
        }

        return cashReceipt;
    }

    @Override
    public Optional<CashReceipt> findById(Long id) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapFromResultSet(resultSet))
                        : Optional.empty();
            }
        }
    }

    private CashReceipt mapFromResultSet(ResultSet resultSet) throws SQLException {
        CashReceipt cashReceipt = new CashReceipt();

        cashReceipt.setId(resultSet.getLong("id"));
        cashReceipt.setCashier(resultSet.getString("cashier"));
        cashReceipt.setDate(resultSet.getDate("date"));
        cashReceipt.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        cashReceipt.setDiscount(resultSet.getBigDecimal("discount"));
        cashReceipt.setAmount(resultSet.getBigDecimal("amount"));
        cashReceipt.setBarcode(resultSet.getString("barcode"));

        return cashReceipt;
    }
}
