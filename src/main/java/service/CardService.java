package service;

import dao.CardDaoInterface;
import dao.impl.CardDao;
import entity.Card;
import exception.EntityNotFoundException;
import exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class CardService {

    private final CardDaoInterface dao;

    public CardService() {
        dao = new CardDao();
    }

    public Card getById(Long id) {
        try {
            return dao.findById(id).orElseThrow(()
                    -> new EntityNotFoundException(String.format("Card with id: %s not found", id)));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
