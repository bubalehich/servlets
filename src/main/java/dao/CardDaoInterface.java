package dao;

import entity.Card;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface CardDaoInterface {

    Collection<Card> findAll(Integer pageNumber, Integer pageSize) throws SQLException;

    Optional<Card> findById(Long id) throws SQLException;

    Card save(Card discountCard) throws SQLException;

    boolean delete(Long id) throws SQLException;
}
