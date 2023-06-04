package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Position {

    private Long id;

    private Item item;

    private CashReceipt cashReceipt;

    private int count;
}
