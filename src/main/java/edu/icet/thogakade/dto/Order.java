package edu.icet.thogakade.dto;

import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Order {
    private String orderId;
    private String date;
    private String customerId;
    private ObservableList<OrderDetails> list;
}
