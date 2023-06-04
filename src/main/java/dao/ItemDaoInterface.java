package dao;

import entity.Item;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface ItemDaoInterface {

    Collection<Item> findAll(Integer pageNumber, Integer pageSize) throws SQLException;

    Optional<Item> findById(Long id) throws SQLException;

    Item save(Item item) throws SQLException;

    boolean update(Item item) throws SQLException;

    boolean delete(Long id) throws SQLException;
}
