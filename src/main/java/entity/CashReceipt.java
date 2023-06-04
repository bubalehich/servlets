package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CashReceipt {

    private Long id;

    private String barcode;

    private String cashier;

    private List<Position> positions = new LinkedList<>();

    private BigDecimal totalAmount;

    private BigDecimal amount;

    private BigDecimal discount;

    private Date date;

    private Card card;

    public void addPosition(Position position) {
        positions.add(position);
    }
}