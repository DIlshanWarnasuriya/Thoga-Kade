package edu.icet.thogakade.bo.custom;

import edu.icet.thogakade.bo.SuperBo;
import edu.icet.thogakade.dto.Customer;
import edu.icet.thogakade.dto.OrderDetails;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CustomerBo extends SuperBo {
    boolean saveCustomer(Customer dto) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(Customer dto, String id) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
    Customer searchCustomer(String id) throws SQLException, ClassNotFoundException;
    ObservableList<Customer> getAllCustomer() throws SQLException, ClassNotFoundException;
}
