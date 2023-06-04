package controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CashReceiptViewModel {

    private String cashier;

    private Date date;

    private List<PositionViewModel> positions;

    private BigDecimal amount;

    private BigDecimal discount;

    private BigDecimal totalAmount;
}
