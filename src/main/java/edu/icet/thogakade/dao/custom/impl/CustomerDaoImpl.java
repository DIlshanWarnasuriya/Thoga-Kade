package edu.icet.thogakade.dao.custom.impl;

import edu.icet.thogakade.dao.custom.CustomerDao;
import edu.icet.thogakade.dto.Customer;
import edu.icet.thogakade.entity.CustomerEntity;
import edu.icet.thogakade.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public boolean save(CustomerEntity entity) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?,?)";
        return CrudUtil.execute(sql,
                entity.getCustID(),
                entity.getCustTitle(),
                entity.getCustName(),
                entity.getDOB(),
                entity.getSalary(),
                entity.getCustAddress(),
                entity.getCity(),
                entity.getProvince(),
                entity.getPostalCode()
        );
    }

    @Override
    public boolean update(CustomerEntity entity, String id) throws SQLException, ClassNotFoundException {
        String sql = "update Customer set CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?, City=?, Province=?, PostalCode=? where CustID=?";
        return CrudUtil.execute(sql,
                entity.getCustTitle(),
                entity.getCustName(),
                entity.getDOB(),
                entity.getSalary(),
                entity.getCustAddress(),
                entity.getCity(),
                entity.getProvince(),
                entity.getPostalCode(),
                id);
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "delete from Customer where CustID = ?";
        return CrudUtil.execute(sql, id);
    }

    @Override
    public CustomerEntity search(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE CustID = ?";
        ResultSet res = CrudUtil.execute(sql, id);
        if (res.next()) {
            return new CustomerEntity(
                    id,
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

    @Override
    public ObservableList<CustomerEntity> getAll() throws SQLException, ClassNotFoundException {
        ObservableList<CustomerEntity> allRecords = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Customer";

        ResultSet res = CrudUtil.execute(sql);
        while (res.next()) {
            allRecords.add(new CustomerEntity(
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
}
