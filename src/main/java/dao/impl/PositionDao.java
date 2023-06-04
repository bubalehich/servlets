package dao.impl;

import dao.AbstractDao;
import dao.PositionDaoInterface;
import dao.dto.DaoPositionDto;
import entity.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class PositionDao extends AbstractDao implements PositionDaoInterface {

    private static final String SAVE = "INSERT INTO position (count, cash_receipt_id, item_id) VALUES (?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM position WHERE id = ?";
    private static final String FIND_BY_CASH_RECEIPT = "SELECT * FROM position WHERE cash_receipt_id = ?";

    @Override
    public Position save(Position position) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, position.getCount());
            preparedStatement.setLong(2, position.getCashReceipt().getId());
            preparedStatement.setLong(3, position.getItem().getId());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                position.setId((long) resultSet.getInt(1));
            }
        }

        return position;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public List<DaoPositionDto> findByCashReceiptId(Long id) throws SQLException {
        var positionsDtoList = new ArrayList<DaoPositionDto>();
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CASH_RECEIPT)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    positionsDtoList.add(mapFromResultSet(resultSet));
                }
            }
        }

        return positionsDtoList;
    }

    private DaoPositionDto mapFromResultSet(ResultSet resultSet) throws SQLException {
        var dto = new DaoPositionDto();

        dto.setId(resultSet.getLong("id"));
        dto.setCount(resultSet.getInt("count"));
        dto.setItemId(resultSet.getLong("item_id"));
        dto.setActive(resultSet.getBoolean("is_active"));
        dto.setCashReceiptId(resultSet.getLong("cash_receipt_id"));

        return dto;
    }
}
