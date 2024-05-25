package edu.icet.thogakade.controller.customer;

import edu.icet.thogakade.db.DBConnection;
import edu.icet.thogakade.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController implements CustomerService{

    public static CustomerController instance;

    private CustomerController(){}

    public static CustomerController getInstance(){
        if (instance==null){
            instance = new CustomerController();
        }
        return instance;
    }

    public Customer searchCustomer(String cusId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM Customer WHERE CustID = ?");
        stm.setString(1, cusId);
        ResultSet res = stm.executeQuery();
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

    public int AddCustomer(Customer customer) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?,?)");
        stm.setString(1, customer.getCustID());
        stm.setString(2, customer.getCustTitle());
        stm.setString(3, customer.getCustName());
        stm.setObject(4, customer.getDOB());
        stm.setDouble(5, customer.getSalary());
        stm.setString(6, customer.getCustAddress());
        stm.setString(7, customer.getCity());
        stm.setString(8, customer.getProvince());
        stm.setString(9, customer.getPostalCode());

        return stm.executeUpdate();
    }

    public int UpdateCustomer(Customer customer, String id) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("update Customer set CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?, City=?, Province=?, PostalCode=? where CustID=?");
        stm.setString(1, customer.getCustTitle());
        stm.setString(2, customer.getCustName());
        stm.setObject(3, customer.getDOB());
        stm.setDouble(4, customer.getSalary());
        stm.setString(5, customer.getCustAddress());
        stm.setString(6, customer.getCity());
        stm.setString(7, customer.getProvince());
        stm.setString(8, customer.getPostalCode());
        stm.setString(9, id);

        return stm.executeUpdate();
    }

    public int DeleteCustomer(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("delete from Customer where CustID = ?");
        stm.setObject(1, id);
        return stm.executeUpdate();
    }
}
