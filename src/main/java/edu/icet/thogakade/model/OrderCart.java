package edu.icet.thogakade.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCart {
    private String code;
    private String description;
    private int qty;
    private double price;
    private double total;
}
