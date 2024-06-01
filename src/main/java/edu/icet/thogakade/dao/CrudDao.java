package edu.icet.thogakade.dao;

import edu.icet.thogakade.dto.Customer;
import edu.icet.thogakade.entity.CustomerEntity;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CrudDao <T> extends SuperDao{

    boolean save(T entity) throws SQLException, ClassNotFoundException;
    boolean update(T entity, String id) throws SQLException, ClassNotFoundException;
    boolean delete(String id) throws SQLException, ClassNotFoundException;
    CustomerEntity search(String id) throws SQLException, ClassNotFoundException;
    ObservableList<CustomerEntity> getAll() throws SQLException, ClassNotFoundException;

}
