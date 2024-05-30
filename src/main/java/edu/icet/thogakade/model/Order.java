package edu.icet.thogakade.model;

import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@AllArgsConstructor
@Data
public class Order {
    private String orderId;
    private String date;
    private String customerId;
    private ObservableList<OrderDetails> list;
}
