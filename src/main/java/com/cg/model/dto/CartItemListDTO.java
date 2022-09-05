package com.cg.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CartItemListDTO {

    private Long id;
    private String productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal amount;
    private String fileUrl;
}
