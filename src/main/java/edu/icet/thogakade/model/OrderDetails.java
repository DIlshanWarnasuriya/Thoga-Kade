package edu.icet.thogakade.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderDetails {
    private String orderId;
    private String itemCode;
    private int qty;
    private int discount;
}
