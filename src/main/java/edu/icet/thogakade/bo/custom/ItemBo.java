package edu.icet.thogakade.bo.custom;

import edu.icet.thogakade.bo.SuperBo;
import edu.icet.thogakade.dto.Customer;
import edu.icet.thogakade.dto.Item;
import edu.icet.thogakade.dto.OrderDetails;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ItemBo extends SuperBo {
    boolean saveItem(Item dto) throws SQLException, ClassNotFoundException;
    boolean updateItem(Item dto, String id) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    Item searchItem(String id) throws SQLException, ClassNotFoundException;
    ObservableList<Item> getAllItem() throws SQLException, ClassNotFoundException;
    boolean updateStock(ObservableList<OrderDetails> list) throws SQLException, ClassNotFoundException;
}
