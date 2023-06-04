package dao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DaoPositionDto {

    private long id;
    private boolean isActive;
    private int count;
    private long cashReceiptId;
    private long itemId;
}
