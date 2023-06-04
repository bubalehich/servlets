package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {

    private Long id;

    private String barcode;

    private boolean isActive;

    private String description;

    private BigDecimal price;

    private boolean isOnDiscount;
}
