package edu.icet.thogakade.controller.item;

import edu.icet.thogakade.dto.Item;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ItemService {
    ObservableList<Item> getAllItems() throws SQLException, ClassNotFoundException;
    Item searchItem(String id) throws SQLException, ClassNotFoundException;
    Boolean saveItem(Item item) throws SQLException, ClassNotFoundException;
    Boolean updateItem(Item item) throws SQLException, ClassNotFoundException;
    Boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
}
