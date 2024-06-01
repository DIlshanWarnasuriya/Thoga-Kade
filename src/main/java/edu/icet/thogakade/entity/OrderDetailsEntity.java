package edu.icet.thogakade.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderDetailsEntity {
    private String orderId;
    private String itemCode;
    private int qty;
    private int discount;
}
