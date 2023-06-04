package service;

import controller.model.CashReceiptMutationModel;
import dao.CashReceiptDaoInterface;
import dao.impl.CashReceiptDao;
import entity.CashReceipt;
import entity.Position;
import exception.EntityNotFoundException;
import exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import mapper.PositionMapper;
import util.CashReceiptCalculator;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class CashReceiptService {

    private final CashReceiptDaoInterface dao;

    private final ItemService itemService;

    private final PositionMapper mapper;

    private final CardService cardService;

    private final PositionService positionService;

    private final CashReceiptCalculator calculator;

    public CashReceiptService() {
        dao = new CashReceiptDao();
        itemService = new ItemService();
        calculator = new CashReceiptCalculator();
        mapper = new PositionMapper();
        cardService = new CardService();
        positionService = new PositionService();
    }

    public CashReceipt create(CashReceiptMutationModel model) {
        CashReceipt cashReceipt = new CashReceipt();

        model.getItemQuantityList().stream()
                .map(mapper::mapToPositionDtoFromString)
                .map(p -> new Position(null, itemService.getById(Long.parseLong(p.getItem())), cashReceipt, p.getCount()))
                .forEach(cashReceipt::addPosition);

        var card = cardService.getById(Long.parseLong(model.getCardNumber()));

        cashReceipt.setCard(card);
        cashReceipt.setDate(new Date());
        cashReceipt.setCashier("Dummy Name");
        cashReceipt.setBarcode(UUID.randomUUID().toString());

        var calculations = calculator.calculate(cashReceipt);

        cashReceipt.setAmount(calculations.amount());
        cashReceipt.setDiscount(calculations.discount());
        cashReceipt.setTotalAmount(calculations.totalAmount());

        try {
            dao.save(cashReceipt);
        } catch (SQLException e) {
            log.error("Can't save CashReceipt: ", e);
            throw new ServiceException(e);
        }
        cashReceipt.getPositions().forEach(positionService::save);

        return cashReceipt;
    }

    public CashReceipt getById(Long id) {
        try {
            var cashReceipt = dao.findById(id).orElseThrow(()
                    -> new EntityNotFoundException(String.format("Item with id: %s not found", id)));

            cashReceipt.setPositions(positionService.getByCashReceiptId(id));

            return cashReceipt;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
