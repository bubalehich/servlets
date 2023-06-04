package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
public class CashReceiptDto {

    private String cardNumber;
    private List<PositionDto> positions = new LinkedList<>();

    public void addPosition(String item, int count) {
        positions.add(new PositionDto(item, count));
    }

    public boolean hasCard() {
        return cardNumber != null && !cardNumber.isEmpty();
    }
}
