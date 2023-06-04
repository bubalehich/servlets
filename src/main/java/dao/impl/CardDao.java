package dao.impl;

import dao.AbstractDao;
import dao.CardDaoInterface;
import entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CardDao extends AbstractDao implements CardDaoInterface {

    private static final String FIND_BY_ID = "SELECT * FROM card WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM card ORDER BY id LIMIT ? OFFSET ?";
    private static final String SAVE = "INSERT INTO card (barcode, holder_name, is_active) VALUES (?, ?, ?)";
    private static final String DELETE = "DELETE FROM card WHERE id = ?";

    @Override
    public List<Card> findAll(Integer pageNumber, Integer pageSize) throws SQLException {
        List<Card> cards = new ArrayList<>();

        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, pageNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    cards.add(mapFromResultSet(resultSet));
                }
            }
        }

        return cards;
    }

    @Override
    public Optional<Card> findById(Long id) throws SQLException {
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
    public Card save(Card card) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, card.getBarcode());
            preparedStatement.setString(2, card.getHolderName());
            preparedStatement.setBoolean(3, card.isActive());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                card.setId((long) resultSet.getInt(1));
            }
        }

        return card;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    private Card mapFromResultSet(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getLong("id"));
        card.setBarcode(resultSet.getString("barcode"));
        card.setActive(resultSet.getBoolean("is_active"));

        return card;
    }
}
