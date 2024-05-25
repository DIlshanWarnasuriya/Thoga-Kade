package edu.icet.thogakade.controller.customer;

import edu.icet.thogakade.model.Customer;

import java.sql.SQLException;

public interface CustomerService {
    Customer searchCustomer(String cusId)  throws SQLException, ClassNotFoundException;
}
