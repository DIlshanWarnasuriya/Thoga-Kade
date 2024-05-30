package edu.icet.thogakade.controller.order;

import java.sql.SQLException;

public interface OrderService {
    int generateNewOrderId() throws SQLException, ClassNotFoundException;
}
