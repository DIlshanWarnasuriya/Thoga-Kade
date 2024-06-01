package edu.icet.thogakade.dao.custom.impl;

import edu.icet.thogakade.dao.custom.ItemDao;
import edu.icet.thogakade.dto.Item;
import edu.icet.thogakade.dto.OrderDetails;
import edu.icet.thogakade.entity.ItemEntity;
import edu.icet.thogakade.entity.OrderDetailsEntity;
import edu.icet.thogakade.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDaoImpl implements ItemDao {

    @Override
    public boolean save(ItemEntity entity) throws SQLException, ClassNotFoundException {
        String sql = "insert into item values(?,?,?,?,?)";

        return CrudUtil.execute(
                sql,
                entity.getCode(),
                entity.getDescription(),
                entity.getSize(),
                entity.getPrice(),
                entity.getQty()
        );
    }

    @Override
    public boolean update(ItemEntity entity, String code) throws SQLException, ClassNotFoundException {
        String sql = "update item set Description=?, PackSize=?, UnitPrice=?, QtyOnHand=? where ItemCode=?";

        return CrudUtil.execute(
                sql,
                entity.getDescription(),
                entity.getSize(),
                entity.getPrice(),
                entity.getQty(),
                code
        );
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        String sql = "delete from item where ItemCode=?";
        return CrudUtil.execute(sql, code);
    }

    @Override
    public ItemEntity search(String code) throws SQLException, ClassNotFoundException {
        String sql = "select * from Item where ItemCode = ?";
        ResultSet resultSet = CrudUtil.execute(sql, code);

        if (resultSet.next()) {

            return new ItemEntity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5)
            );
        }
        return null;
    }

    @Override
    public ObservableList<ItemEntity> getAll() throws SQLException, ClassNotFoundException {
        ObservableList<ItemEntity> itemList = FXCollections.observableArrayList();
        String sql = "select * from Item";
        ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {

            itemList.add(new ItemEntity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5)
            ));
        }
        return itemList;
    }

    @Override
    public boolean updateStock(ObservableList<OrderDetailsEntity> list) throws SQLException, ClassNotFoundException {
        String sql = "update item set QtyOnHand=QtyOnHand-? where ItemCode=?";
        for (OrderDetailsEntity entity : list) {

            boolean isUpdateItem = CrudUtil.execute(sql, entity.getQty(), entity.getItemCode());
            if (!isUpdateItem) {
                return false;
            }
        }
        return true;
    }
}
