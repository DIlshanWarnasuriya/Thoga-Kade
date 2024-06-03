package edu.icet.thogakade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class OrderDetails {
    private String orderId;
    private String itemCode;
    private int qty;
    private int discount;
}
