package edu.icet.thogakade.bo.custom.impl;

import edu.icet.thogakade.bo.custom.ItemBo;
import edu.icet.thogakade.dao.DaoFactory;
import edu.icet.thogakade.dao.custom.ItemDao;
import edu.icet.thogakade.dto.Item;
import edu.icet.thogakade.dto.OrderDetails;
import edu.icet.thogakade.entity.ItemEntity;
import edu.icet.thogakade.entity.OrderDetailsEntity;
import edu.icet.thogakade.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;

public class ItemBoImpl implements ItemBo {

    private final ItemDao itemDao = DaoFactory.getInstance().getDao(DaoType.ITEM);

    @Override
    public boolean saveItem(Item dto) throws SQLException, ClassNotFoundException {
        return itemDao.save(new ModelMapper().map(dto, ItemEntity.class));
    }

    @Override
    public boolean updateItem(Item dto, String code) throws SQLException, ClassNotFoundException {
        return itemDao.update(new ModelMapper().map(dto, ItemEntity.class), code);
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return itemDao.delete(code);
    }

    @Override
    public Item searchItem(String code) throws SQLException, ClassNotFoundException {
        return new ModelMapper().map(itemDao.search(code), Item.class);
    }

    @Override
    public ObservableList<Item> getAllItem() throws SQLException, ClassNotFoundException {
        ObservableList<Item> itemList = FXCollections.observableArrayList();

        for (ItemEntity entity : itemDao.getAll()){
            itemList.add(new ModelMapper().map(entity, Item.class));
        }
        return itemList;
    }

    @Override
    public boolean updateStock(ObservableList<OrderDetails> list) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDetailsEntity> itemList = FXCollections.observableArrayList();
        for (OrderDetails dto : list){
            itemList.add(new ModelMapper().map(dto, OrderDetailsEntity.class));
        }
        return itemDao.updateStock(itemList);
    }


}
