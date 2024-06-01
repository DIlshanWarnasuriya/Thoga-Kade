package edu.icet.thogakade.dao.custom;

import edu.icet.thogakade.dao.CrudDao;
import edu.icet.thogakade.entity.ItemEntity;
import edu.icet.thogakade.entity.OrderDetailsEntity;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ItemDao extends CrudDao<ItemEntity> {
    boolean updateStock(ObservableList<OrderDetailsEntity> list) throws SQLException, ClassNotFoundException;
}
