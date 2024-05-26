package edu.icet.thogakade.controller.customer;

import edu.icet.thogakade.crudUtil.CrudUtil;
import edu.icet.thogakade.db.DBConnection;
import edu.icet.thogakade.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController implements CustomerService {

    public static CustomerController instance;

    private CustomerController() {
    }

    public static CustomerController getInstance() {
        if (instance == null) {
            instance = new CustomerController();
        }
        return instance;
    }

    public ObservableList<Customer> getAllCustomer() throws SQLException, ClassNotFoundException {
        ObservableList<Customer> allRecords = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Customer";

        ResultSet res = CrudUtil.execute(sql);
        while (res.next()) {
            allRecords.add(new Customer(
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getDate(4),
                    res.getDouble(5),
                    res.getString(6),
                    res.getString(7),
                    res.getString(8),
                    res.getString(9)));
        }
        return allRecords;
    }

    public Customer searchCustomer(String cusId) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Customer WHERE CustID = ?";
        ResultSet res = CrudUtil.execute(sql, cusId);
        if (res.next()) {
            return new Customer(
                    cusId,
                    res.getString(2),
                    res.getString(3),
                    res.getDate(4),
                    res.getDouble(5),
                    res.getString(6),
                    res.getString(7),
                    res.getString(8),
                    res.getString(9)
            );
        }
        return null;
    }

    public Boolean addCustomer(Customer customer) throws SQLException, ClassNotFoundException {

        String sql = "INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?,?)";
        return CrudUtil.execute(sql,
                customer.getCustID(),
                customer.getCustTitle(),
                customer.getCustName(),
                customer.getDOB(),
                customer.getSalary(),
                customer.getCustAddress(),
                customer.getCity(),
                customer.getProvince(),
                customer.getPostalCode()
        );
    }

    public Boolean updateCustomer(Customer customer, String id) throws SQLException, ClassNotFoundException {

        String sql = "update Customer set CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?, City=?, Province=?, PostalCode=? where CustID=?";
        return CrudUtil.execute(sql,
                customer.getCustTitle(),
                customer.getCustName(),
                customer.getDOB(),
                customer.getSalary(),
                customer.getCustAddress(),
                customer.getCity(),
                customer.getProvince(),
                customer.getPostalCode(),
                id);
    }

    public Boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {

        String sql = "delete from Customer where CustID = ?";
        return CrudUtil.execute(sql, id);
    }
}
