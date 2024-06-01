package edu.icet.thogakade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Item {
    private String code;
    private String description;
    private String size;
    private double price;
    private int qty;
}
