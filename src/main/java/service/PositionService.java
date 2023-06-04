package service;

import dao.PositionDaoInterface;
import dao.impl.PositionDao;
import entity.Position;
import exception.ServiceException;
import factory.ServiceFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PositionService {

    private final PositionDaoInterface dao;

    private final ItemService itemService;
    private final CashReceiptService cashReceiptService;

    public PositionService() {
        dao = new PositionDao();
        itemService = ServiceFactory.getInstance().getItemService();
        cashReceiptService = ServiceFactory.getInstance().getCashReceiptService();
    }

    public Position save(Position position) {
        try {
            return dao.save(position);
        } catch (SQLException e) {
            log.error("Can't save position: ", e);
            throw new ServiceException(e);
        }
    }

    public List<Position> getByCashReceiptId(Long cashReceiptId) {
        try {
            var positionsDtoList = dao.findByCashReceiptId(cashReceiptId);
            var cashReceipt = cashReceiptService.getById(cashReceiptId);

            return positionsDtoList.stream()
                    .map(dto -> new Position(dto.getId(), itemService.getById(dto.getId()), cashReceipt, dto.getCount()))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
