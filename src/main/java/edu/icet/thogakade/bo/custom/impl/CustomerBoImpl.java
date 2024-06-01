package edu.icet.thogakade.bo.custom.impl;

import edu.icet.thogakade.bo.custom.CustomerBo;
import edu.icet.thogakade.dao.custom.CustomerDao;
import edu.icet.thogakade.dao.DaoFactory;
import edu.icet.thogakade.dto.Customer;
import edu.icet.thogakade.entity.CustomerEntity;
import edu.icet.thogakade.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;

public class CustomerBoImpl implements CustomerBo {

    private final CustomerDao customerDao = DaoFactory.getInstance().getDao(DaoType.CUSTOMER);

    @Override
    public boolean saveCustomer(Customer dto) throws SQLException, ClassNotFoundException {
        return customerDao.save(new ModelMapper().map(dto, CustomerEntity.class));
    }

    @Override
    public boolean updateCustomer(Customer dto, String id) throws SQLException, ClassNotFoundException {
        return customerDao.update(new ModelMapper().map(dto, CustomerEntity.class), id);
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDao.delete(id);
    }

    @Override
    public Customer searchCustomer(String id) throws SQLException, ClassNotFoundException {
        return new ModelMapper().map(customerDao.search(id), Customer.class);
    }

    @Override
    public ObservableList<Customer> getAllCustomer() throws SQLException, ClassNotFoundException {
        ObservableList<Customer> customersList = FXCollections.observableArrayList();

        for (CustomerEntity entity : customerDao.getAll()){
            customersList.add(new ModelMapper().map(entity, Customer.class));
        }
        return customersList;
    }


}
