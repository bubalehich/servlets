package mapper;

import controller.model.CashReceiptViewModel;
import controller.model.PositionViewModel;
import entity.CashReceipt;

import java.math.BigDecimal;

public class CashReceiptMapper {

    public CashReceiptViewModel mapToViewFromCashReceipt(CashReceipt receipt) {
        return CashReceiptViewModel.builder()
                .cashier(receipt.getCashier())
                .date(receipt.getDate())
                .positions(receipt.getPositions().stream()
                        .map(p -> new PositionViewModel(
                                p.getItem().getDescription(),
                                p.getCount(),
                                p.getItem().getPrice().multiply(BigDecimal.valueOf(p.getCount())))).
                        toList())
                .totalAmount(receipt.getTotalAmount())
                .amount(receipt.getAmount())
                .discount(receipt.getDiscount())
                .build();
    }
}
