package dao.impl;

import dao.AbstractDao;
import dao.ItemDaoInterface;
import entity.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ItemDao extends AbstractDao implements ItemDaoInterface {

    private static final String FIND_BY_ID = "SELECT * FROM item WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM item ORDER BY id LIMIT ? OFFSET ?";
    private static final String SAVE = "INSERT INTO item (barcode, description, is_active, is_on_discount, price) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM item WHERE id = ?";
    private static final String UPDATE = "UPDATE item SET barcode = ?, description = ?, is_active = ?, is_on_discount = ?, price = ? WHERE id = ?";

    @Override
    public Collection<Item> findAll(Integer pageNumber, Integer pageSize) throws SQLException {
        List<Item> items = new ArrayList<>();

        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, pageNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapFromResultSet(resultSet));
                }
            }
        }

        return items;
    }

    @Override
    public Optional<Item> findById(Long id) throws SQLException {
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

    @Override
    public Item save(Item item) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, item.getBarcode());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setBoolean(3, item.isActive());
            preparedStatement.setBoolean(4, item.isOnDiscount());
            preparedStatement.setBigDecimal(5, item.getPrice());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                item.setId((long) resultSet.getInt(1));
            }
        }

        return item;
    }

    @Override
    public boolean update(Item item) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, item.getBarcode());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setBoolean(3, item.isActive());
            preparedStatement.setBoolean(4, item.isOnDiscount());
            preparedStatement.setBigDecimal(5, item.getPrice());
            preparedStatement.setLong(6, item.getId());

            return preparedStatement.executeUpdate() > 1;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    private Item mapFromResultSet(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setBarcode(resultSet.getString("barcode"));
        item.setDescription(resultSet.getString("description"));
        item.setPrice(resultSet.getBigDecimal("price"));
        item.setOnDiscount(resultSet.getBoolean("is_on_discount"));
        item.setActive(resultSet.getBoolean("is_active"));

        return item;
    }
}
