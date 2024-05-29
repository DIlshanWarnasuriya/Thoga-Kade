package edu.icet.thogakade.controller.placeOrder;

import java.sql.SQLException;

public interface PlaceOrderService {
    int generateNewOrderId() throws SQLException, ClassNotFoundException;
}
