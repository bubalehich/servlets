package util;


import entity.CashReceipt;
import entity.Position;

import java.math.BigDecimal;

public class CashReceiptCalculator {
    
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(0.1);
    private static final int ITEMS_COUNT_FOR_DISCOUNT = 5;

    public CalculatorResult calculate(CashReceipt cashReceipt) {
        boolean hasDiscount = cashReceipt.getCard() != null
                && cashReceipt.getPositions().stream()
                .filter(p -> p.getItem().isOnDiscount())
                .map(Position::getCount)
                .reduce(0, Integer::sum)
                >= ITEMS_COUNT_FOR_DISCOUNT;

        var amount = cashReceipt.getPositions().stream()
                .map(p -> p.getItem().getPrice().multiply(new BigDecimal(p.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var discount = hasDiscount
                ? amount.multiply(DISCOUNT)
                : BigDecimal.ZERO;

        var totalAmount = hasDiscount
                ? amount.subtract(discount)
                : amount;

        return new CalculatorResult(amount, discount, totalAmount);
    }
}
