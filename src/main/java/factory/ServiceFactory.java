package factory;

import service.CardService;
import service.CashReceiptService;
import service.ItemService;
import service.PositionService;

public class ServiceFactory {

    private final CardService cardService;

    private final CashReceiptService cashReceiptService;

    private final ItemService itemService;

    private final PositionService positionService;

    private ServiceFactory() {
        cardService = new CardService();
        cashReceiptService = new CashReceiptService();
        itemService = new ItemService();
        positionService = new PositionService();
    }

    private static class ServiceFactorySingletonHolder {
        static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return ServiceFactorySingletonHolder.INSTANCE;
    }

    public CardService getCardService() {
        return cardService;
    }

    public CashReceiptService getCashReceiptService() {
        return cashReceiptService;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public PositionService getPositionService() {
        return positionService;
    }
}
