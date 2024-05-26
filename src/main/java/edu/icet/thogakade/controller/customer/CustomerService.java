package edu.icet.thogakade.controller.customer;

import edu.icet.thogakade.model.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CustomerService {
    ObservableList<Customer> getAllCustomer() throws SQLException, ClassNotFoundException;
    Customer searchCustomer(String cusId)  throws SQLException, ClassNotFoundException;
    Boolean addCustomer(Customer customer) throws SQLException, ClassNotFoundException;
    Boolean updateCustomer(Customer customer, String id) throws SQLException, ClassNotFoundException;
    Boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
}
