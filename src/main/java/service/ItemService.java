package service;

import dao.ItemDaoInterface;
import dao.impl.ItemDao;
import entity.Item;
import exception.EntityNotFoundException;
import exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class ItemService {

    private final ItemDaoInterface dao;

    public ItemService() {
        dao = new ItemDao();
    }

    public Item getById(Long id) {
        try {
            return dao.findById(id).orElseThrow(()
                    -> new EntityNotFoundException(String.format("Item with id: %s not found", id)));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
